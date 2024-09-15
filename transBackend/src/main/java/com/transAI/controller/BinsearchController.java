package com.transAI.controller;

import com.transAI.pojo.Result;
import com.transAI.service.BinsearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subtask/binsearch")
public class BinsearchController {

    @Autowired
    private BinsearchService binsearchService;

    @GetMapping("/check")
    public Result<Boolean> check() {
        return Result.success(binsearchService.check());
    }

    @GetMapping("query")
    public Result<Integer> query() {
        return Result.success(binsearchService.query());
    }
}
