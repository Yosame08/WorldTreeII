package com.transAI.service.impl;

import com.transAI.service.ZhengyanService;
import com.transAI.utils.DateLogger;
import com.transAI.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ZhengyanServiceImpl implements ZhengyanService {
    @Autowired
    private final TartsServiceImpl tartsServiceImpl;

    public ZhengyanServiceImpl(TartsServiceImpl tartsServiceImpl) {
        this.tartsServiceImpl = tartsServiceImpl;
    }

    @Override
    public int validate(List<Integer> list) {
        Map<String, Object> map = ThreadLocalUtil.get();
        System.out.println("[" + DateLogger.getTime() + " Zhengyan] " + map.get("username") + " validates list " + list);
        // 查询是否 0 - 8所有数字都出现一次且仅一次
        boolean[] flag = new boolean[9];
        for (int i = 0; i < 9; i++) {
            if (list.get(i) == null) {
                return -1;
            }
            flag[i] = false;
        }
        for (int i = 0; i < 9; i++) {
            if (list.get(i) < 0 || list.get(i) > 8) {
                return -1;
            }
            if (flag[list.get(i)]) {
                return 0;
            }
            flag[list.get(i)] = true;
        }
        for (int i = 0; i < 9; i++) {
            if (!flag[i]) {
                return 0;
            }
        }

        // 查询3*3矩阵每一行每一列的和
        int[] rowSum = new int[3];
        int[] colSum = new int[3];
        for (int i = 0; i < 3; i++) {
            rowSum[i] = 0;
            colSum[i] = 0;
        }
        for (int i = 0; i < 9; i++) {
            rowSum[i / 3] += list.get(i);
            colSum[i % 3] += list.get(i);
        }
        for (int i = 0; i < 3; i++) {
            if (rowSum[i] != 12 || colSum[i] != 12) {
                return 0;
            }
        }

        // 查询对角线的和
        if (list.get(0) + list.get(4) + list.get(8) != 9 || list.get(2) + list.get(4) + list.get(6) != 21) {
            return 0;
        }

        tartsServiceImpl.passTask(16, true);
        return 1;
    }
}
