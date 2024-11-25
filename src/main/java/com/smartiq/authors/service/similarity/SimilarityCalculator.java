package com.smartiq.authors.service.similarity;

import java.util.Map;

public interface SimilarityCalculator {
    double calculateSimilarity(Map<String, Long> inputRoots, Map<String, Long> wordFrequency);
}