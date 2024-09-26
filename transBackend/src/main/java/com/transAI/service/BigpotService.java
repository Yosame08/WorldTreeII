package com.transAI.service;

import com.transAI.pojo.bigpot.Bigpot;
import com.transAI.pojo.bigpot.BigpotResult;

public interface BigpotService {
    Bigpot init(int id);

    BigpotResult cook(int id, String gameToken, int x, int y, int operator);
}
