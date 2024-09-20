package com.transAI.controller;

import com.transAI.pojo.Bigpot;
import com.transAI.pojo.BigpotResult;
import com.transAI.pojo.Result;
import com.transAI.service.BigpotService;
import com.transAI.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/cook")
    public Result<BigpotResult> cook(@RequestBody Map<String, Object> params) {
        Map<String, Object> map = ThreadLocalUtil.get();
        int id = (int) map.get("id");
        String game_token = (String) params.get("gameToken");
        int x = (int) params.get("x");
        int y = (int) params.get("y");
        int operator = (int) params.get("operator");
        BigpotResult bigpotResult = bigpotService.cook(id, game_token, x, y, operator);
        return Result.success(bigpotResult);

    }
}
