package com.smartiq.authors.service.similarity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimilarityCalculatorFactory {

    private final CosineSimilarityCalculator cosineSimilarityCalculator;
    private final BasicSimilarityCalculator basicSimilarityCalculator;

    @Autowired
    public SimilarityCalculatorFactory(CosineSimilarityCalculator cosineSimilarityCalculator,
                                       BasicSimilarityCalculator basicSimilarityCalculator) {
        this.cosineSimilarityCalculator = cosineSimilarityCalculator;
        this.basicSimilarityCalculator = basicSimilarityCalculator;
    }

    public SimilarityCalculator getCalculator(String algorithm) {
        if ("cosine".equalsIgnoreCase(algorithm)) {
            return cosineSimilarityCalculator;
        } else if ("basic".equalsIgnoreCase(algorithm)) {
            return basicSimilarityCalculator;
        } else {
            throw new IllegalArgumentException("Invalid algorithm type: " + algorithm);
        }
    }
}