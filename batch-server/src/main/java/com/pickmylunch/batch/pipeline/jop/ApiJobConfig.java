package com.pickmylunch.batch.pipeline.jop;

import com.pickmylunch.batch.pipeline.step.RawRestaurantItemWriter;
import com.pickmylunch.batch.pipeline.step.RestaurantItemProcessor;
import com.pickmylunch.batch.pipeline.step.RawRestaurantItemReader;
import com.pickmylunch.batch.pipeline.step.RestaurantItemWriter;
import com.pickmylunch.common.entity.*;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ApiJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;
    private final RawRestaurantItemReader rawRestaurantItemReader;
    private final RestaurantItemProcessor restaurantItemProcessor;
    private final RawRestaurantItemWriter rawRestaurantItemWriter;
    private final RestaurantItemWriter restaurantItemWriter;

    @Value("${api.page-size}")
    private int apiPageSize;

    @Bean
    public Job publicRestaurantApiJob(Step fetchRestaurantDataStep, Step restaurantProcessingStep) {
        return new JobBuilder("publicRestaurantApiJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(fetchRestaurantDataStep)
                .next(restaurantProcessingStep)
                .build();
    }

    @Bean
    public Step fetchRestaurantDataStep() {
        return new StepBuilder("fetchRestaurantDataStep", jobRepository)
                .<RawRestaurant, RawRestaurant>chunk(apiPageSize, transactionManager)
                .reader(rawRestaurantItemReader)
                .writer(rawRestaurantItemWriter)
                .build();
    }

    @Bean
    public Step restaurantProcessingStep() {
        return new StepBuilder("restaurantProcessingStep", jobRepository)
                .<RawRestaurant, Restaurant>chunk(apiPageSize, transactionManager)
                .reader(rawRestaurantDbItemReader(entityManagerFactory))
                .processor(restaurantItemProcessor)
                .writer(restaurantItemWriter)
                .build();
    }

    @Bean
    public JpaPagingItemReader<RawRestaurant> rawRestaurantDbItemReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<RawRestaurant>()
                .name("rawRestaurantDbItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(apiPageSize)
                .queryString("SELECT r FROM RawRestaurant r WHERE r.isUpdated = true")
                .build();
    }
    
}
