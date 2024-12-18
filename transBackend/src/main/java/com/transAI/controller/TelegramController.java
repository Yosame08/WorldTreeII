package com.transAI.controller;

import com.transAI.pojo.Result;
import com.transAI.pojo.TelegramRecord;
import com.transAI.pojo.User;
import com.transAI.pojo.UserPair;
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

    @PostMapping("/get_played")
    public Result<Integer> getPlayed(@RequestBody UserPair userPair) {
        System.out.println(userPair);
        return Result.success(telegramService.getPlayed(userPair));
    }

    @PostMapping("/submit")
    public Result submit(@RequestBody TelegramRecord telegramRecord) {
//        if (telegramRecord.getRank() != 998244000) return Result.error("auth error"); // 约定的特殊值
//        return telegramService.submit(telegramRecord);
        return Result.error("活动已经结束");
    }
}
