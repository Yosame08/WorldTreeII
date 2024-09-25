package com.transAI.scheduler;

import com.transAI.pojo.Visiting;
import com.transAI.service.TaskService;
import com.transAI.service.VisitingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TaskScheduler {

    @Autowired
    private VisitingService visitingService;

    @Scheduled(cron = "0 59 23 25 9 ?")
    public void markTaskAsExpired() {
//        taskService.markTaskAsExpiredAndScore();
    }
}