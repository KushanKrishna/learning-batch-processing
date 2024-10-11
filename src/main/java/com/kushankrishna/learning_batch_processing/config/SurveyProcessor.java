package com.kushankrishna.learning_batch_processing.config;

import com.kushankrishna.learning_batch_processing.model.AnnualEnterpriseSurvey;
import org.springframework.batch.item.ItemProcessor;

public class SurveyProcessor implements ItemProcessor<AnnualEnterpriseSurvey, AnnualEnterpriseSurvey> {


    @Override
    public AnnualEnterpriseSurvey process(AnnualEnterpriseSurvey item) throws Exception {
        return item;
    }
}
