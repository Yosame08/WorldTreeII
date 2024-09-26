package com.transAI.controller;

import com.transAI.pojo.nim.*;
import com.transAI.pojo.Result;
import com.transAI.service.NimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subtask/nim")
public class NimController {

    @Autowired
    private NimService nimService;

    @PostMapping("/init")
    public Result<NimInit> check(@RequestBody NimInitRequest nimInitRequest) {
        return Result.success(nimService.init(nimInitRequest));
    }

    @PostMapping("/step")
    public Result<NimResult> step(@RequestBody NimRequest nim) {
        var result = nimService.step(nim);
        if (result.getStep().getNum() == -1){
            return Result.error("发送的请求无效");
        }
        return Result.success(result);
    }

}
