package com.transAI.controller;

import com.transAI.pojo.Result;
import com.transAI.pojo.bigpot.Bigpot;
import com.transAI.pojo.bigpot.Bigpot2;
import com.transAI.pojo.bigpot.BigpotResult;
import com.transAI.service.Bigpot2Service;
import com.transAI.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/subtask/xiaolongbao")
public class Bigpot2Controller {

    @Autowired
    private Bigpot2Service bigpot2Service;
    @GetMapping("/init")
    public Result<Bigpot2> init() {
        Map<String, Object> map = ThreadLocalUtil.get();
        int id = (int) map.get("id");
        return Result.success(bigpot2Service.init(id));
    }

    @PostMapping("/cook")
    public Result<BigpotResult> cook(@RequestBody Map<String, Object> params) {
        Map<String, Object> map = ThreadLocalUtil.get();
        int id = (int) map.get("id");
        String game_token = (String) params.get("gameToken");
        int x = (int) params.get("x");
        int y = (int) params.get("y");
        int operator = (int) params.get("operator");
        BigpotResult bigpotResult = bigpot2Service.cook(id, game_token, x, y, operator);
        if (bigpotResult.getPass() == -1){
            return Result.error("无效的操作");
        }
        return Result.success(bigpotResult);

    }
}
