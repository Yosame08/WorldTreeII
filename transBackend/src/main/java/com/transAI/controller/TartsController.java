package com.transAI.controller;

import com.transAI.pojo.Result;
import com.transAI.service.TartsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subtask/tarts")
public class TartsController {

    @Autowired
    private TartsService tartsService;

    @GetMapping("/w0w_f1n1sh3d")
    public Result checkTarts() {
//        tartsService.add();
//        return Result.success();
        return Result.error("活动已经结束");
    }
}
