package com.transAI.controller;

import com.transAI.pojo.Result;
import com.transAI.service.SkittlesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subtask/skittles")
public class SkittlesController {

    @Autowired
    private SkittlesService skittlesService;

    @PostMapping("/init")
    public Result<List<Integer>> init(@RequestBody Map<String, Integer> params) {
//        Integer start = (Integer) params.get("start");
//        return Result.success(skittlesService.init(start));
        return Result.error("活动已经结束");
    }
}
