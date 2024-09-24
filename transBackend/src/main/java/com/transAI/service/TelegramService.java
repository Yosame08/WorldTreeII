package com.transAI.service;

import com.transAI.pojo.Result;
import com.transAI.pojo.TelegramRecord;
import com.transAI.pojo.UserPair;

import java.util.List;

public interface TelegramService {

    List<TelegramRecord> getTelegramRecordList();

    Integer getPlayed(UserPair userPair);

    Result submit(TelegramRecord telegramRecord);
}
