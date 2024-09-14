package com.transAI.service.impl;

import com.transAI.mapper.TelegramMapper;
import com.transAI.mapper.UserMapper;
import com.transAI.pojo.Result;
import com.transAI.pojo.TelegramRecord;
import com.transAI.service.TelegramService;
import com.transAI.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TelegramServiceImpl implements TelegramService {

    @Autowired
    private TelegramMapper telegramMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<TelegramRecord> getTelegramRecordList() {
        var telegramRecordList = telegramMapper.getTelegramRecordList();
        // 设置telegramRecordList的rank的值
        for (int i = 0; i < telegramRecordList.size(); i++) {
            telegramRecordList.get(i).setRank(i + 1);
        }
        return telegramRecordList;
    }

    @Override
    public List<Integer> getPlayed() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        String name = (String) map.get("username");
        return telegramMapper.getPlayed(name);
    }

    @Override
    public Result submit(TelegramRecord telegramRecord) {

        telegramMapper.submit(telegramRecord);
        return Result.success();
    }
}
