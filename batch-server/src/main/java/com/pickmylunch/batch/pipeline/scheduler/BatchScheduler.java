package com.pickmylunch.batch.pipeline.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.*;
import org.springframework.batch.core.repository.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.Component;

import java.time.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @Scheduled(cron = "0 20 5 * * *")
    public void runPublicRestaurantApiJob() throws Exception {
        log.info("[Scheduled] runPublicRestaurantApiJob 실행 시작: " + LocalDateTime.now());
        try {
            Job apiJob = jobRegistry.getJob("publicRestaurantApiJob");
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(apiJob, params);
        } catch (NoSuchJobException e) {
            log.error("잡을 찾을 수 없습니다: {}", e.getMessage());
        } catch (JobExecutionAlreadyRunningException e) {
            log.warn("이미 실행 중인 잡이 있습니다: {}", e.getMessage());
        } catch (JobInstanceAlreadyCompleteException e) {
            log.warn("잡이 이미 완료된 상태입니다: {}", e.getMessage());
        } catch (JobRestartException e) {
            log.error("잡 재시작 오류: {}", e.getMessage());
        } catch (JobParametersInvalidException e) {
            log.error("잡 파라미터가 유효하지 않습니다: {}", e.getMessage());
        } catch (Exception e) {
            log.error("잡 실행 중 오류 발생: ", e);
        }
    }
}