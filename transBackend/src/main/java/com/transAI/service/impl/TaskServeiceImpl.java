package com.transAI.service.impl;

import com.transAI.mapper.TaskMapper;
import com.transAI.pojo.Task;
import com.transAI.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServeiceImpl implements TaskService {

    @Autowired
    TaskMapper taskMapper;

    @Override
    public List<Task> getTaskList() {
        return taskMapper.getTaskList();
    }
}
