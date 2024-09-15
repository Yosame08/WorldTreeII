package com.transAI.controller;

import com.transAI.pojo.Result;
import com.transAI.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/util")
public class UtilController {
    @Autowired
    private UtilService utilService;

    @GetMapping("/now_time")
    public Result<LocalDateTime> getNowTime() {
        return Result.success(utilService.getNowTime());
    }

}
