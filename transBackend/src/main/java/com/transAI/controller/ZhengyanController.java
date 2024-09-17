package com.transAI.controller;

import com.transAI.pojo.Result;
import com.transAI.service.ZhengyanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subtask/zhengyan")
public class ZhengyanController {

    @Autowired
    private ZhengyanService zhengyanService;

    @PostMapping("/validate")
    public Result<Boolean> validate(@RequestBody Map<String, Object> params) {
        List<Integer> list = (List<Integer>) params.get("answer");
        int flag = zhengyanService.validate(list);
        if(flag < 0) {
            return Result.error("The answer is invalid");
        }
        return Result.success(flag == 1);
    }
}
