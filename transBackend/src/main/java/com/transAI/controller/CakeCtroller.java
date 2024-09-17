package com.transAI.controller;

import com.transAI.pojo.Result;
import com.transAI.service.CakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/subtask/cake")
public class CakeCtroller {

    @Autowired
    private CakeService cakeService;

    @RequestMapping("/submit")
    public Result<String> submit(@RequestBody Map<String, String> params) {
        String answer = params.get("answer");
        String flag = cakeService.submit(answer);
        return Result.success(flag);
    }
}
