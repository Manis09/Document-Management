package com.mahadiscom.document_management.service.impl;

import com.mahadiscom.document_management.service.MigrationService;
import com.mahadiscom.document_management.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerServiceImpl implements SchedulerService {

    private final MigrationService migrationService;

    @Override
    @Scheduled(cron = "0 0 2 * * ?")
    public void runMigration(){
        log.info("Scheduling has started at : {}", LocalDateTime.now());
        migrationService.processOldDocuments();
    }
}
