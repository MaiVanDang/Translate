package com.hust.itss1.config;

import com.hust.itss1.service.TranslationHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class TranslationHistoryCleanupScheduler {
    private final TranslationHistoryService translationHistoryService;

    @Value("${app.translation-history.retention-seconds:1800}")
    private int retentionSeconds;

    @Value("${app.translation-history.cleanup-batch-size:1000}")
    private int batchSize;

    @Scheduled(cron = "${app.translation-history.cleanup-cron:*/10 * * * * *}")
    public void cleanOldTranslationHistories() {
        LocalDateTime cutoff = LocalDateTime.now().minusSeconds(retentionSeconds);
        long deletedCount = translationHistoryService.deleteOldTranslationHistories(cutoff, batchSize);
    }
}
