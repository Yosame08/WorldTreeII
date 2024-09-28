package com.transAI.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.transAI.mapper.Bigpot2Mapper;
import com.transAI.pojo.bigpot.Bigpot2;
import com.transAI.pojo.bigpot.BigpotResult;
import com.transAI.service.Bigpot2Service;
import com.transAI.utils.DateLogger;
import com.transAI.utils.MathEx;
import com.transAI.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class Bigpot2ServiceImpl implements Bigpot2Service {

    @Autowired
    private Bigpot2Mapper bigpot2Mapper;

    @Autowired
    private TartsServiceImpl tartsServiceImpl;

    @Override
    public Bigpot2 init(int id) {
        Bigpot2 newGame;
        Gson gson = new Gson();
        if (bigpot2Mapper.checkHave(id) == 0){
            newGame = getBigpot(1);
            bigpot2Mapper.newGame(id, 1, newGame.getGameToken(), gson.toJson(newGame.getNumbers()));
        }
        else {
            int level = bigpot2Mapper.checkLevel(id);
            newGame = getBigpot(level);
            bigpot2Mapper.replaceGame(id, level, newGame.getGameToken(), gson.toJson(newGame.getNumbers()));
        }
        return newGame;
    }


    private Bigpot2 getBigpot(int level) {
        Bigpot2 bigpot = null;
        String randomToken = UUID.randomUUID().toString();
        switch(level) {
            case 1:
                bigpot = new Bigpot2(randomToken, 1, Arrays.asList(1, 2, 2, 2, 2, 2));
                break;
            case 2:
                bigpot = new Bigpot2(randomToken, 2, Arrays.asList(2, 2, 2, 3, 3, 3));
                break;
            case 3:
                bigpot = new Bigpot2(randomToken, 3, Arrays.asList(6, 6, 9, 9, 9, 9));
                break;
            case 4:
                bigpot = new Bigpot2(randomToken, 4, Arrays.asList(30, 31, 32, 33, 40, 50));
                break;
            default:
                assert false;
        }
        Map<String, Object> map = ThreadLocalUtil.get();
        System.out.println("[" + DateLogger.getTime() + " Xiaolongbao] " + map.get("username") + " start a new level " + level + " game with token " + randomToken);
        return bigpot;
    }

    @Override
    public BigpotResult cook(int id, String gameToken, int x, int y, int operator) {
        String numberList = bigpot2Mapper.getNumbers(gameToken);
        // 遍历变量，找到x和y的值，并从list中删掉
        List<Integer> numbers = new Gson().fromJson(numberList, new TypeToken<List<Integer>>(){}.getType());
        boolean flagx = false, flagy = false;
        for (int i = 0; i < numbers.size(); i++) {
            if (!flagx && numbers.get(i) == x) {
                flagx = true;
                numbers.remove(i);
                i--;
                continue;
            }
            if (!flagy && numbers.get(i) == y) {
                flagy = true;
                numbers.remove(i);
                i--;
            }
        }
        if(!flagx || !flagy) {
            return new BigpotResult(-1, -1);
        }

        int newInt = 0;
        switch (operator) {
            case 0:
                newInt = MathEx.gcd(x, y);
                break;
            case 1:
                newInt = MathEx.lcm(x, y);
                break;
            case 2:
                newInt = x + y;
                break;
            default:
                assert false;
        }
        numbers.add(newInt);


        BigpotResult bigpotResult = new BigpotResult();
        if (numbers.size() == 1 && newInt == 24){
            bigpotResult.setPass(1);
            bigpotResult.setResult(24);
            int nowLevel = bigpot2Mapper.checkLevel(id);
            bigpot2Mapper.replaceGame(id, nowLevel==4?4:nowLevel+1, "", "");
            if (nowLevel == 4){
                tartsServiceImpl.passTask(17, true);
            }
        }
        else {
            bigpotResult.setPass(0);
            bigpotResult.setResult(newInt);
            bigpot2Mapper.replaceGame(id, bigpot2Mapper.checkLevel(id), gameToken, new Gson().toJson(numbers));
        }

        Map<String, Object> map = ThreadLocalUtil.get();
        System.out.println("[" + DateLogger.getTime() + " Xiaolongbao] User " + map.get("username") + ": cook " + x + (operator==0?" GCD ":(operator==1?" LCM ":" PLUS ")) + y + ", now: " + new Gson().toJson(numbers));
        return bigpotResult;
    }
}
