package com.transAI.scheduler;

import com.transAI.service.VisitingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TaskExpireScheduler {

    @Autowired
    private VisitingService visitingService;

    @Scheduled(cron = "59 59 23 25 9 ?")
    public void markTaskAsExpired() {
        visitingService.expireAndJudge(0);
    }

    @Scheduled(cron = "00 55 23 2 10 ?")
    public void markTaskAsExpired2() {
        visitingService.expireAndJudge(1);
    }
}