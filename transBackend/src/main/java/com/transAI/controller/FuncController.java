package com.transAI.controller;

import com.transAI.pojo.Result;
import com.transAI.pojo.Sticker;
import com.transAI.pojo.UserPoint;
import com.transAI.pojo.UserTotalPoint;
import com.transAI.service.FuncService;
import com.transAI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/func")
public class FuncController {

    @Autowired
    private FuncService funcService;

    @GetMapping("/rank")
    public Result<List<UserPoint>> getRank() {
        return Result.success(funcService.getRank());
    }

    @PostMapping("get_user_trend")
    public Result <List<UserTotalPoint>> getUserTrend(@RequestBody Map<String, String> params) {
        int userId = Integer.parseInt(params.get("userId"));
        return Result.success(funcService.getUserTrend(userId));
    }

    @GetMapping("get_stickers")
    public Result<List<Sticker>> getStickers() {
        return Result.success(funcService.getStickers());
    }

    @PostMapping("modify_stickers")
    public Result modifyStickers(@RequestBody Sticker sticker) {
        funcService.modifyStickers(sticker);
        return Result.success();
    }
}
