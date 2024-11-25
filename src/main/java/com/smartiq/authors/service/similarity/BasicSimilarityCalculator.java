package com.smartiq.authors.service.similarity;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BasicSimilarityCalculator implements SimilarityCalculator {

    @Override
    public double calculateSimilarity(Map<String, Long> inputRoots, Map<String, Long> wordFrequency) {
        double score = 0;

        for (Map.Entry<String, Long> item : inputRoots.entrySet()) {
            String root = item.getKey();
            long inputFrequency = item.getValue();

            if (wordFrequency.containsKey(root)) {
                long authorFrequency = wordFrequency.get(root);
                score += inputFrequency + authorFrequency;
            }
        }

        return score;
    }
}
