package com.transAI.service;

import com.transAI.pojo.UserPoint;
import com.transAI.pojo.UserTotalPoint;

import java.util.List;

public interface FuncService {
    List<UserPoint> getRank();

    List<UserTotalPoint> getUserTrend(int userId);
}