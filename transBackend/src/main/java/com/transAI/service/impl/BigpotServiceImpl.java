package com.transAI.service.impl;

import com.transAI.mapper.BigpotMapper;
import com.transAI.pojo.bigpot.Bigpot;
import com.transAI.pojo.bigpot.BigpotResult;
import com.transAI.utils.DateLogger;
import com.transAI.service.BigpotService;
import com.transAI.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class BigpotServiceImpl implements BigpotService {

    @Autowired
    private BigpotMapper bigpotMapper;


    @Autowired
    private TartsServiceImpl tartsServiceImpl;

    @Override
    public Bigpot init(int id) {
        int level = bigpotMapper.getLevel(id);

        Bigpot bigpot = getBigpot(level);

        return bigpot;
    }


    private Bigpot getBigpot(int level) {
        Bigpot bigpot = null;
        String randomToken = UUID.randomUUID().toString();

//        String randomToken = "a";
        switch(level) {
            case 1:
                bigpot = new Bigpot(randomToken, 1, 4, 2, 3, 9, 16);
                break;
            case 2:
                bigpot = new Bigpot(randomToken,2,  4, 3, 7, 15, 31);
                break;
            case 3:
                bigpot = new Bigpot(randomToken,3,  4, 7, 25, 27, 29);
                break;
            case 4:
                bigpot = new Bigpot(randomToken,4,  4, 23,43,54,109);
                break;
            default:
                return null; // 或者你可以抛出异常
        }
        bigpotMapper.insert(bigpot);
        Map<String, Object> map = ThreadLocalUtil.get();
        System.out.println("[" + DateLogger.getTime() + " Big Pot] " + map.get("username") + " start a new level " + level + " game with token " + randomToken);
        return bigpot;
    }

    @Override
    public BigpotResult cook(int id, String gameToken, int x, int y, int operator) {
        Bigpot bigpot = bigpotMapper.getBigpot(id, gameToken);
        if(bigpot == null) {
            return null;
        }
        int len = bigpot.getLen();
        int[] arr = new int[4];
        int index = 0;
        if(bigpot.getX() > 0) {
            arr[index++] = bigpot.getX();
        }
        if(bigpot.getY() > 0) {
            arr[index++] = bigpot.getY();
        }
        if(bigpot.getZ() > 0) {
            arr[index++] = bigpot.getZ();
        }
        if(bigpot.getW() > 0) {
            arr[index++] = bigpot.getW();
        }
        if(index != len) {
            return null;
        }
        boolean flagx = false, flagy = false;
        int indexx = -1, indexy = -1;
        for(int i = 0;i < len;i++) {
            if(arr[i] == x) {
                flagx = true;
                indexx = i;
                continue;
            }
            if(arr[i] == y) {
                flagy = true;
                indexy = i;
                continue;
            }
        }
        if(!flagx || !flagy) {
            return null;
        }
        BigpotResult bigpotResult = new BigpotResult();

        int result = 0;
        switch (operator) {
            case 0:
                result = arr[indexx] & arr[indexy];
                break;
            case 1:
                result = arr[indexx] | arr[indexy];
                break;
            case 2:
                result = arr[indexx] ^ arr[indexy];
                break;
            default:
                return null;
        }
        arr[indexx] = result;
        arr[indexy] = -1;

        bigpot.setLen(len - 1);

        bigpot.setX(arr[0]);
        bigpot.setY(arr[1]);
        bigpot.setZ(arr[2]);
        bigpot.setW(arr[3]);

        bigpotMapper.update(bigpot);

        if(len == 2 && result == 24) {
            bigpotResult.setPass(1);
            int level = bigpotMapper.getLevel(id);
            if (level != 4) {
                bigpotMapper.updateLevel(id, level + 1);
            } else {
                tartsServiceImpl.passTask(3, true);
            }
        } else {
            bigpotResult.setPass(0);
        }
        bigpotResult.setResult(result);
        Map<String, Object> map = ThreadLocalUtil.get();
        System.out.println("[" + DateLogger.getTime() + " Big Pot] User " + map.get("username") + ": cook " + x + (operator==0?" AND ":(operator==1?" OR ":" XOR ")) + y + ", status: " + bigpot);
        return bigpotResult;
    }
}
