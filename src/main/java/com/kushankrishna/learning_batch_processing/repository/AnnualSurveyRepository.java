package com.kushankrishna.learning_batch_processing.repository;

import com.kushankrishna.learning_batch_processing.model.AnnualEnterpriseSurvey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnualSurveyRepository extends JpaRepository<AnnualEnterpriseSurvey, Long> {
}
