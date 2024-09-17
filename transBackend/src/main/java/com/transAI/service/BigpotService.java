package com.transAI.service;

import com.transAI.pojo.Bigpot;
import com.transAI.pojo.BigpotResult;

public interface BigpotService {
    Bigpot init(int id);

    BigpotResult cook(int id, String gameToken, int x, int y, int operator);
}
