package com.smartiq.authors.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zemberek.morphology.TurkishMorphology;

@Configuration
public class TurkishMorphologyConfig {

    @Bean
    public TurkishMorphology turkishMorphology() {
        return TurkishMorphology.createWithDefaults();
    }

}
