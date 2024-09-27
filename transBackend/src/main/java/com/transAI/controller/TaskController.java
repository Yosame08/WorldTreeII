package com.transAI.controller;

import com.transAI.pojo.Result;
import com.transAI.pojo.Task;
import com.transAI.service.TaskService;
import com.transAI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/get_task_list")
    public Result<List<Task>> getTaskList() {
        return Result.success(taskService.getTaskList());
    }

    @PostMapping("/submit")
    public Result<Boolean> submitTask(@RequestBody Map<String, Object> map) {
        int taskId = (int) map.get("taskId");
        String flag = (String) map.get("answer");
        boolean checkExpire = taskService.checkExpire(taskId);
        if (checkExpire) {
            return Result.error("任务已过期");
        }
        return Result.success(taskService.submitTask(taskId, flag));
    }

    @PostMapping("hint")
    public Result<String> hint(@RequestBody Map<String, Object> map) {
        int taskId = (int) map.get("taskId");
        String tmp = taskService.hint(taskId);
        if(tmp.equals("货币不足")) {
            return Result.error(tmp);
        }
        return Result.success(tmp);
    }

    @PostMapping("clue")
    public Result<String> clue(@RequestBody Map<String, Object> map) {
        int taskId = (int) map.get("taskId");
        return Result.success(taskService.clue(taskId));
    }
}
