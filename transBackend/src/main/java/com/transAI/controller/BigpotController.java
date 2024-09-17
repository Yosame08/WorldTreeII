package com.transAI.controller;

import com.transAI.pojo.Bigpot;
import com.transAI.pojo.Result;
import com.transAI.service.BigpotService;
import com.transAI.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/subtask/bigpot")
public class BigpotController {

    @Autowired
    private BigpotService bigpotService;
    @GetMapping("/init")
    public Result<Bigpot> init() {
        Map<String, Object> map = ThreadLocalUtil.get();
        int id = (int) map.get("id");
        return Result.success(bigpotService.init(id));
    }
}
