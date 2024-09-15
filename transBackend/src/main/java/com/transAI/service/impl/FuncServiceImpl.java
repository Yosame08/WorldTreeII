package com.transAI.service.impl;

import com.transAI.mapper.UserMapper;
import com.transAI.mapper.UserTotalPointMapper;
import com.transAI.pojo.User;
import com.transAI.pojo.UserPoint;
import com.transAI.pojo.UserTotalPoint;
import com.transAI.service.FuncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FuncServiceImpl implements FuncService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserTotalPointMapper userTotalPointMapper;

    @Override
    public List<UserPoint> getRank() {
        List<User> userList = userMapper.getUserSortByPoint();
        List<UserPoint> userPointList = new ArrayList<>();

        if (userList.isEmpty()) {
            return userPointList; // 返回空列表
        }

        int rank = 1; // 当前排名
        int currentRankCount = 1; // 记录相同 point 的用户个数
        int previousPoint = userList.get(0).getPoint(); // 初始化为第一个用户的 point

        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            UserPoint userPoint = new UserPoint();

            // 如果当前用户的 point 和前一个用户的 point 不同，更新 rank
            if (user.getPoint() != previousPoint) {
                rank = currentRankCount;
                previousPoint = user.getPoint();
            }

            // 填充 UserPoint 对象
            userPoint.setRank(rank);
            userPoint.setUserId(user.getId());
            userPoint.setUsername(user.getUsername());
            userPoint.setPoint(user.getPoint());

            userPointList.add(userPoint);

            currentRankCount++; // 更新当前的计数
        }

        return userPointList;
    }

    @Override
    public List<UserTotalPoint> getUserTrend(int userId) {
        return userTotalPointMapper.getUserTrend(userId);
    }
}
