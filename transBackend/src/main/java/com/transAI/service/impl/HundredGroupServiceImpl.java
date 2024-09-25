package com.transAI.service.impl;

import com.transAI.mapper.UserMapper;
import com.transAI.pojo.UploadTaskRecord;
import com.transAI.service.HundredGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HundredGroupServiceImpl implements HundredGroupService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TartsServiceImpl tartsServiceImpl;

    @Override
    public boolean uploadTaskRecord(UploadTaskRecord record) {
        int id = userMapper.getId(record.username);
        if (id == 0) {
            return false;
        }
        tartsServiceImpl.userPassesTask(id, record.taskId);
        return true;
    }
}
