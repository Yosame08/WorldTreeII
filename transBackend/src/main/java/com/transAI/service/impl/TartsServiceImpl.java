package com.transAI.service.impl;

import com.transAI.mapper.TaskMapper;
import com.transAI.mapper.TaskUserMapper;
import com.transAI.mapper.UserMapper;
import com.transAI.mapper.UserTotalPointMapper;
import com.transAI.pojo.TaskUser;
import com.transAI.pojo.UserTotalPoint;
import com.transAI.service.TartsService;
import com.transAI.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Transactional
public class TartsServiceImpl implements TartsService {
    @Autowired
    private TaskUserMapper taskUserMapper;

    @Autowired
    private UserTotalPointMapper userTotalPointMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void add() {
        Map<String, Object> map = ThreadLocalUtil.get();

        int id = (int) map.get("id");
        int taskId = 0;

        TaskUser taskUser = taskUserMapper.getTaskUser(id, taskId);
        if(taskUser == null) {
            int score = 100;
            taskUser = new TaskUser();
            taskUser.setUserId(id);
            taskUser.setTaskId(taskId);
            taskUser.setStatus(1);
            taskUser.setPoint(score);
            taskUser.setTime(LocalDateTime.now());
            taskUserMapper.insert(taskUser);
            int pre_point = userTotalPointMapper.getMaxPoint(id);
            // 输出pre_point

            userTotalPointMapper.addPoint(id,pre_point + score);
        }
    }


    public void passTask(int taskId) {
        Map<String, Object> map = ThreadLocalUtil.get();
        int id = (int) map.get("id");

        TaskUser taskUser = taskUserMapper.getTaskUser(id, taskId);
        if(taskUser != null) {
            return ;
        }
        int score = 100;
        taskUser = new TaskUser();
        taskUser.setUserId(id);
        taskUser.setTaskId(taskId);
        taskUser.setStatus(1);
        taskUser.setPoint(score);
        taskUser.setTime(LocalDateTime.now());
        taskUserMapper.insert(taskUser);

        int pre_point = userTotalPointMapper.getMaxPoint(id);
        // 输出pre_point

        userTotalPointMapper.addPoint(id,pre_point + score);

        userMapper.updatePoint(id, pre_point + score);
    }
}
