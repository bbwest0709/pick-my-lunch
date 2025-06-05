package com.pickmylunch.batch.pipeline.jop;

import com.pickmylunch.batch.pipeline.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.*;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ApiJobConfig {

    private final ApiService apiService;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job publicRestaurantApiJob(Step fetchRestaurantDataStep) {
        return new JobBuilder("publicRestaurantApiJob", jobRepository)
                .start(fetchRestaurantDataStep)
                .next(saveRestaurantDataStep())
                .build();
    }

    @Bean
    public Step fetchRestaurantDataStep() {
        return new StepBuilder("fetchRestaurantDataStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    apiService.executeRawDataLoad();
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step saveRestaurantDataStep() {
        return new StepBuilder("saveRestaurantDataStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    apiService.processRawToRestaurant();
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
