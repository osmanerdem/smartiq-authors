package com.smartiq.authors.service.similarity;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CosineSimilarityCalculator implements SimilarityCalculator {

    @Override
    public double calculateSimilarity(Map<String, Long> inputRoots, Map<String, Long> wordFrequency) {
        double dotProduct = inputRoots.entrySet().stream()
                .filter(entry -> wordFrequency.containsKey(entry.getKey()))
                .mapToDouble(entry -> entry.getValue() * wordFrequency.get(entry.getKey()))
                .sum();

        double magnitude1 = Math.sqrt(inputRoots.values().stream()
                .mapToDouble(count -> count * count)
                .sum());

        double magnitude2 = Math.sqrt(wordFrequency.values().stream()
                .mapToDouble(count -> count * count)
                .sum());

        return magnitude1 == 0 || magnitude2 == 0 ? 0 : (dotProduct / (magnitude1 * magnitude2));
    }
}