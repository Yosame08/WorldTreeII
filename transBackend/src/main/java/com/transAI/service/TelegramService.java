package com.transAI.service;

import com.transAI.pojo.Result;
import com.transAI.pojo.TelegramRecord;

import java.util.List;

public interface TelegramService {

    List<TelegramRecord> getTelegramRecordList();

    List<Integer> getPlayed();

    Result submit(TelegramRecord telegramRecord);
}
