package com.pickmylunch.api.domain.restaurant.scheduler;

import com.pickmylunch.api.domain.restaurant.cache.ViewCountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TotalViewCountCacheScheduler {

    private final ViewCountService viewCountService;

    @Scheduled(cron = "0 0 3 * * * ")
    public void refreshTotalViewCountCache() {
        log.info("TotalViewCountCacheScheduler: 전체 조회수 기반 캐시 갱신 시작");
        viewCountService.refreshCacheByTotalCount();
        log.info("TotalViewCountCacheScheduler: 전체 조회수 기반 캐시 갱신 완료");
    }

}
