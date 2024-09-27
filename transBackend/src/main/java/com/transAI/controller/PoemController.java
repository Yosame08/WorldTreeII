package com.transAI.controller;

import com.transAI.pojo.Pos;
import com.transAI.pojo.Result;
import com.transAI.pojo.poem.PoemCheck;
import com.transAI.pojo.poem.SendCheck;
import com.transAI.pojo.poem.SendSubmit;
import com.transAI.service.OrientationService;
import com.transAI.service.PoemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/subtask/poem")
public class PoemController {

    @Autowired
    private PoemService poemService;

    @GetMapping("/next")
    public Result<Boolean> next() {
        if (poemService.next()) return Result.success(true);
        else return Result.error("您已经完成了所有的诗");
    }

    @PostMapping("/check")
    public Result<PoemCheck> getPoem(@RequestBody SendCheck times) {
        return Result.success(poemService.getPoem(times.getTimes()));
    }

    @PostMapping("/submit")
    public Result<PoemCheck> submitAnswer(@RequestBody SendSubmit answer) {
        PoemCheck res = poemService.submitAnswer(answer.getAnswer());
        if (res.getResult().equals("-1")){
            return Result.error("您无法继续回答此题");
        }
        return Result.success(res);
    }
}
