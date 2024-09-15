package com.transAI.service.impl;

import com.transAI.mapper.TaskMapper;
import com.transAI.mapper.TaskUserMapper;
import com.transAI.pojo.Task;
import com.transAI.pojo.TaskUser;
import com.transAI.service.TaskService;
import com.transAI.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TaskServeiceImpl implements TaskService {

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    TaskUserMapper taskUserMapper;

    @Override
    public List<Task> getTaskList() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");

        List<Task> taskList = taskMapper.getTaskList();
        List<TaskUser> taskUserList = taskUserMapper.getTaskUserList(id);

        for(Task task : taskList) {
            boolean flag = false;
            for(TaskUser taskUser : taskUserList) {
                if(task.getTaskId().equals(taskUser.getTaskId())) {
                    task.setTaskStatus(taskUser.getStatus());
                    task.setGetPoint(taskUser.getPoint());
                    flag = true;
                    break;
                }
            }
            if(!flag) {
                task.setTaskStatus(0);
                task.setGetPoint(0);
            }
        }

        return taskList;
    }
}
