package com.transAI.controller;

import com.transAI.pojo.Result;
import com.transAI.pojo.Visiting;
import com.transAI.service.VisitingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/subtask/visiting")
public class Visiting2Controller {

    @Autowired
    private VisitingService visitingService;

    @PostMapping("/update")
    public Result update(@RequestBody Visiting visiting) {
        // 判断如果过了2024-10-01 23:59:59就不允许更新
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime deadline = LocalDateTime.of(2024, 10, 1, 23, 59, 59);
        if (now.isAfter(deadline)) {
            return Result.error("已超过提交截止日期");
        }
        visitingService.update(visiting, 1);
        return Result.success();
    }

    @GetMapping("/get_info")
    public Result<Visiting> getInfo() {
        return Result.success(visitingService.getInfo(1));
    }
}
