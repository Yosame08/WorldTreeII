package com.transAI.service;

import com.transAI.pojo.Task;

import java.util.List;

public interface TaskService {

    List<Task> getTaskList();

    boolean submitTask(int taskId, String flag);
}
