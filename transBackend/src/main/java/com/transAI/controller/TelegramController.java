package com.transAI.controller;

import com.transAI.pojo.Result;
import com.transAI.pojo.TelegramRecord;
import com.transAI.service.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subtask/telegram")
public class TelegramController {

    @Autowired
    private TelegramService telegramService;

    @GetMapping("/rank")
    public Result<List<TelegramRecord>> getTaskList() {
        return Result.success(telegramService.getTelegramRecordList());
    }

    @GetMapping("/get_played")
    public Result<List<Integer>> getPlayed() {
        return Result.success(telegramService.getPlayed());
    }

    @PostMapping("/submit")
    public Result submit(@RequestBody TelegramRecord telegramRecord) {
        return telegramService.submit(telegramRecord);
    }
}
