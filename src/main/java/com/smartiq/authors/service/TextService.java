package com.smartiq.authors.service;

import com.smartiq.authors.model.entity.Author;
import com.smartiq.authors.model.entity.Text;
import com.smartiq.authors.repository.AuthorRepository;
import com.smartiq.authors.repository.TextRepository;
import com.smartiq.authors.service.similarity.SimilarityCalculator;
import com.smartiq.authors.service.similarity.SimilarityCalculatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zemberek.morphology.TurkishMorphology;
import zemberek.morphology.analysis.SingleAnalysis;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TextService {

    private final TextRepository textRepository;

    private final AuthorRepository authorRepository;

    private final TurkishMorphology turkishMorphology;

    private final SimilarityCalculatorFactory similarityCalculatorFactory;

    @Autowired
    public TextService(TextRepository textRepository,
                       TurkishMorphology turkishMorphology,
                       AuthorRepository authorRepository,
                       SimilarityCalculatorFactory similarityCalculatorFactory) {
        this.textRepository = textRepository;
        this.turkishMorphology = turkishMorphology;
        this.authorRepository = authorRepository;
        this.similarityCalculatorFactory = similarityCalculatorFactory;
    }

    // Stop-words listesi
    private static final Set<String> STOP_WORDS = Set.of(
            "ve", "bir", "de", "da", "şu", "bu", "o", "gibi", "ile", "ise", "ama", "ki", "ne", "ya", "bununla", "kendi", "için"
    );

    public List<Map.Entry<String, Long>> getMostFrequentWords(Long authorId) {
        // Yazıdaki kelimeleri analiz et ve en çok kullanılan kelimeleri döndür
        List<Text> texts = textRepository.findAllByAuthorId(authorId);

        if (texts.isEmpty()) {
            log.warn("No texts found for authorId: {}", authorId);
            return Collections.emptyList();
        }

        // Tüm yazıların içeriklerini tek bir stringde birleştir
        String allText = texts.stream()
                .map(Text::getContent)
                .map(content -> content.replaceAll("[^\\p{L}\\p{Z}]+", ""))
                .collect(Collectors.joining(" "));
        log.debug("Merged text for authorId {}: {}", authorId, allText);

        // Kelimeleri ayır ve stop-words filtresi uygula
        List<String> words = Arrays.stream(allText.split("[^\\p{L}]+"))
                .map(word -> word.toLowerCase())
                .filter(word -> !STOP_WORDS.contains(word)) // filtrele
                .collect(Collectors.toList());
        log.debug("Filtered words: {}", words);

        List<String> roots = words.stream()
                .map(this::findRoot)
                .collect(Collectors.toList());
        log.debug("Root words: {}", roots);


        // Kelimelerin frekanslarını hesapla
        Map<String, Long> repeatedWordCounts = roots.stream()
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()));

        // En sık kullanılan 30 kelimeyi al
        return repeatedWordCounts.entrySet().stream()
                .sorted(Comparator.comparingLong(Map.Entry<String, Long>::getValue).reversed()) // Sayısına göre sırala
                .limit(30) // İlk 30 kelimeyi al
                .collect(Collectors.toList());
    }

    public Map<String, Object> analyzeTextAndFindAuthor(String text, String algorithm) {
        // 1. Verilen metni analiz ve köklerini bulma
        List<String> inputRoots = Arrays.stream(text.split("[^\\p{L}]+"))
                .map(word -> word.toLowerCase())
                .filter(word -> !STOP_WORDS.contains(word))
                .map(this::findRoot)
                .collect(Collectors.toList());

        // 2. Mevcut yazarların kelime istatistiklerini al
        List<Text> allTexts = textRepository.findAll();

        // 2. Text nesnelerini yazar ID'ye göre grupla
        Map<Long, List<String>> authorTexts = allTexts.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getAuthor().getId(),
                        Collectors.mapping(Text::getContent, Collectors.toList())
                ));

        try {
            SimilarityCalculator calculator = similarityCalculatorFactory.getCalculator(algorithm);


            double highestScore = 0;
            Long mostSimilarAuthorId = 0L;

            for (Map.Entry<Long, List<String>> entry : authorTexts.entrySet()) {
                Long authorId = entry.getKey();
                List<String> contents = entry.getValue();

                Map<String, Long> authorMostFrequentWordsOfAll = calculateWordFrequency(contents);
                Map<String, Long> inputWordFrequency = calculateWordFrequency(inputRoots);

                double score = calculator.calculateSimilarity(inputWordFrequency, authorMostFrequentWordsOfAll);
                if (score > highestScore) {
                    highestScore = score;
                    mostSimilarAuthorId = authorId;
                }

            }

            Optional<Author> responseAuthor = authorRepository.findById(mostSimilarAuthorId);
            return Map.of(
                    "authorId", mostSimilarAuthorId,
                    "authorName", responseAuthor.get().getName(),
                    "similarityScore", highestScore
            );
        } catch (IllegalArgumentException e) {
            return Map.of(
                    "error", e.getMessage()
            );
        }
    }


    private Map<String, Long> calculateWordFrequency(List<String> contents) {
        // Tüm metinleri birleştir ve kelime frekanslarını hesapla
        return contents.stream()
                .flatMap(content -> Arrays.stream(content.split("[^\\p{L}]+"))
                        .map(word -> word.toLowerCase())
                        .filter(word -> !STOP_WORDS.contains(word))
                        .map(this::findRoot))
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()));
    }

    public String findRoot(String word) {

        log.debug("Received input word: {}", word);
        // TurkishMorphology ile kelimenin kökünü bulma
        List<SingleAnalysis> analyses = turkishMorphology.analyze(word).getAnalysisResults();
        if (!analyses.isEmpty()) {

            int i = 0;
            log.info("Lemmas:");
            for (SingleAnalysis analyse : analyses) {
                log.debug(i++ + ": " + analyse.getLemmas());
            }

            String lastLemma = analyses.get(i - 1).getLemmas().get(0);

            //reverse check ile secondary seçeneği olmayan en yakın kelimeyi bulma
            if (i > 1) {
                List<SingleAnalysis> reversedAnalyses = new ArrayList<SingleAnalysis>(analyses);
                Collections.reverse(reversedAnalyses);
                for (SingleAnalysis currentAnalyse : reversedAnalyses) {
                    if (!reversedAnalyses.get(i - 1).getDictionaryItem().secondaryPos.shortForm.toLowerCase().equals("none")) {
                        log.debug("secondaryPos word is not None.go previous index");
                        i--;
                        i = (i < 1) ? analyses.size() : i;
                    } else {
                        lastLemma = analyses.get(i - 1).getLemmas().get(0);
                        break;
                    }
                }
            }

            // kelime Verb ise onun mastarlı halini alma işlemi
            if (analyses.get(i - 1).getMorphemeDataList().get(0).morpheme.name.toLowerCase().equals("verb")) {
                log.debug("this word is a verb");
                lastLemma = analyses.get(i - 1).getDictionaryItem().lemma;
            }

            log.debug("Selected: " + lastLemma);
            return lastLemma;
        }
        return word;
    }

    public void addTextToAuthor(Long authorId, Text text) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Belirtilen ID'ye sahip bir yazar bulunamadı."));
        text.setAuthor(author);
        textRepository.save(text);
    }
}
