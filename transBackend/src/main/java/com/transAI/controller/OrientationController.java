package com.transAI.controller;

import com.transAI.pojo.Orientation;
import com.transAI.pojo.Pos;
import com.transAI.pojo.Result;
import com.transAI.service.OrientationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subtask/orientation")
public class OrientationController {

    @Autowired
    private OrientationService orientationService;

    @GetMapping("/init")
    public Result<List<Orientation>> initOrientation() {
        return Result.success(orientationService.initOrientation());
    }

    @PostMapping("/check")
    public Result<Orientation> checkOrientation(@RequestBody Pos pos) {
        return Result.success(orientationService.checkOrientation(pos));
    }

    @GetMapping("/find")
    public Result<Boolean> findWebsite() {
        return Result.success(orientationService.findWebsite());
    }
}
