package com.transAI.service.impl;

import com.transAI.service.SkittlesService;
import com.transAI.utils.DateLogger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class SkittlesServiceImpl implements SkittlesService {
    private int kLength = 60;

        private int transitionProbability[][] = {
        {0, 0, 0, 0, 1, 0, 0, 0, 0},
        {1, 0, 1, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 1, 0, 0, 0, 0},
        {0, 0, 1, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 1, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 1, 0, 1},
        {0, 1, 0, 1, 0, 1, 0, 1, 0}
    };

        @Override
    public List<Integer> init(int start) {
        List<Integer> list = new ArrayList<>();
        for(int i = 0;i < kLength;i++) {
            list.add(start);
            start = getNext(start);
        }
        System.out.println("[" + DateLogger.getTime() + " Bai, Lang and Bocchi the Rock] Generated list: " + list);
        return list;
    }

    private Integer getNext(int current) {
        Random random = new Random();
        int[] transitions = transitionProbability[current];
        int sum = 0;

        // 计算当前状态的转移总数
        for (int probability : transitions) {
            sum += probability;
        }

        // 随机生成一个数，确定转移到哪个状态
        int rand = random.nextInt(sum);  // 生成一个 0 到 sum-1 的随机数
        int cumulative = 0;

        for (int i = 0; i < transitions.length; i++) {
            cumulative += transitions[i];
            if (rand < cumulative) {
                return i;  // 返回对应的下一个状态
            }
        }

        return current; // 如果没有匹配到，就返回当前状态（通常不应该发生）
    }
}
