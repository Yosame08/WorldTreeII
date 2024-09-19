package com.transAI.controller;

import com.transAI.pojo.Orientation;
import com.transAI.pojo.Pos;
import com.transAI.pojo.Result;
import com.transAI.service.OrientationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subtask/orientation")
public class OrientationController {

    @Autowired
    private OrientationService orientationService;

    @RequestMapping("/check")
    public Result<Orientation> checkOrientation(@RequestBody Pos pos) {
        return Result.success(orientationService.checkOrientation(pos));
    }
}
