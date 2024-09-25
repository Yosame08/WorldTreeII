package com.transAI.service.impl;

import com.transAI.mapper.TelegramMapper;
import com.transAI.mapper.UserMapper;
import com.transAI.pojo.Result;
import com.transAI.pojo.TelegramRecord;
import com.transAI.pojo.User;
import com.transAI.pojo.UserPair;
import com.transAI.service.TelegramService;
import com.transAI.utils.DateLogger;
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

    @Autowired
    private TartsServiceImpl tartsServiceImpl;

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
    public Integer getPlayed(UserPair userPair) {
        System.out.println("in");
        Map<String, Object> map = ThreadLocalUtil.get();
        var played1 = telegramMapper.getPlayed(userPair.getUsernameA());
        var played2 = telegramMapper.getPlayed(userPair.getUsernameB());
        System.out.println("i");
        System.out.println("[" + DateLogger.getTime() + " Telegram Generate Level ] " + userPair.getUsernameA() + " " + played1 + " " + userPair.getUsernameB() + " " + played2);
        // 取并集
        played1.removeAll(played2);
        played1.addAll(played2);
        List<Integer> ok = new java.util.ArrayList<>(List.of(1, 2, 3));
        ok.removeAll(played1);
        System.out.println("n");
        // 从ok中随机取一个，如果ok为空，返回0
        if (ok.isEmpty()) return 0;
        Integer getId = ok.get((int) (Math.random() * ok.size()));
        return getId;
    }

    @Override
    public Result submit(TelegramRecord telegramRecord) {
        System.out.println("[" + DateLogger.getTime() + " Telegram Submission ] " + telegramRecord);
        telegramMapper.submit(telegramRecord);
        int idA = userMapper.getId(telegramRecord.getUsernameA());
        int idB = userMapper.getId(telegramRecord.getUsernameB());
        int score = (int)Math.ceil(200 * Math.tanh((double)telegramRecord.getScore() / 1000));
        tartsServiceImpl.passPartialTask(idA, 10, score, false);
        tartsServiceImpl.passPartialTask(idB, 10, score, false);
        return Result.success();
    }
}
