package com.transAI.controller;

import com.transAI.pojo.Result;
import com.transAI.pojo.Task;
import com.transAI.service.TaskService;
import com.transAI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {


    @Autowired
    private TaskService taskService;

    @GetMapping("/get_task_list")
    public Result<List<Task>> getTaskList() {
        return Result.success(taskService.getTaskList());
    }
}
