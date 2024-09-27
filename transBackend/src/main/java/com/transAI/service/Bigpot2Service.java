package com.transAI.service;

import com.transAI.pojo.bigpot.Bigpot2;
import com.transAI.pojo.bigpot.BigpotResult;

public interface Bigpot2Service {
    Bigpot2 init(int id);

    BigpotResult cook(int id, String gameToken, int x, int y, int operator);
}
