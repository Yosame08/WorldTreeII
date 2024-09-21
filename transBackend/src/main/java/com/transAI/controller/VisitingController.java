package com.transAI.controller;

import com.transAI.pojo.Result;
import com.transAI.pojo.Visiting;
import com.transAI.service.VisitingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subtask/visiting")
public class VisitingController {

    @Autowired
    private VisitingService visitingService;

    @PostMapping("/update")
    public Result update(@RequestBody Visiting visiting) {
        System.out.println("visiting:" + visiting);
        visitingService.update(visiting);
        return Result.success();
    }

    @GetMapping("/get_info")
    public Result<Visiting> getInfo() {
        return Result.success(visitingService.getInfo());
    }
}
