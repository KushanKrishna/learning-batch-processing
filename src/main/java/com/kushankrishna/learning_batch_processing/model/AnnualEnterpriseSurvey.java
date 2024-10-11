package com.kushankrishna.learning_batch_processing.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AnnualEnterpriseSurvey {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    int Year;
    String Industry_aggregation_NZSIOC;
    String Industry_code_NZSIOC;
    String Industry_name_NZSIOC;
    String Units;
    String Variable_code;
    String Variable_name;
    String Variable_category;
    String Value;
    String Industry_code_ANZSIC06;
}
