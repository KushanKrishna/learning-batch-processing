package com.kushankrishna.learning_batch_processing.config;

import com.kushankrishna.learning_batch_processing.model.AnnualEnterpriseSurvey;
import com.kushankrishna.learning_batch_processing.repository.AnnualSurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final AnnualSurveyRepository repository;

    @Bean
    public FlatFileItemReader<AnnualEnterpriseSurvey> itemReader() {
        FlatFileItemReader<AnnualEnterpriseSurvey> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/annual-enterprise-survey.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(linemapper());
        return itemReader;
    }

    @Bean
    public SurveyProcessor processor() {
        return new SurveyProcessor();
    }

    @Bean
    public RepositoryItemWriter<AnnualEnterpriseSurvey> writer() {
        RepositoryItemWriter<AnnualEnterpriseSurvey> writer = new RepositoryItemWriter<>();
        writer.setRepository(repository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Step importStep() {
        return new StepBuilder("importCSV", jobRepository)
                .<AnnualEnterpriseSurvey, AnnualEnterpriseSurvey>chunk(1000, platformTransactionManager)
                .reader(itemReader())
                .processor(processor())
                .writer(writer())
                .taskExecutor(taskExecutor())
                .build();
    }
    @Bean
    public Job runJob(){
        return new JobBuilder("importSurvey",jobRepository)
                .start(importStep())
                .build();
    }
    @Bean
    public TaskExecutor taskExecutor(){
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
        taskExecutor.setConcurrencyLimit(20);
        return taskExecutor;
    }

    private LineMapper<AnnualEnterpriseSurvey> linemapper() {
        DefaultLineMapper<AnnualEnterpriseSurvey> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setNames("Year", "Industry_aggregation_NZSIOC", "Industry_code_NZSIOC", "Industry_name_NZSIOC", "Units", "Variable_code", "Variable_name", "Variable_category", "Value", "Industry_code_ANZSIC06");
        BeanWrapperFieldSetMapper<AnnualEnterpriseSurvey> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(AnnualEnterpriseSurvey.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }


}
