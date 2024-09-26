package com.transAI.service.impl;

import com.google.gson.Gson;
import com.transAI.mapper.NimMapper;
import com.transAI.pojo.nim.*;
import com.transAI.service.NimService;
import com.transAI.utils.ThreadLocalUtil;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NimServiceImpl implements NimService {

    private final NimMapper nimMapper;

    public NimServiceImpl(NimMapper nimMapper) {
        this.nimMapper = nimMapper;
    }

    @Override
    public NimInit init(NimInitRequest request){
        int randLen = (int) (Math.random() * 10) % 4 + 5;
        // generate random array with xor sum is 0
        List<Integer> randArray = new ArrayList<>();
        int xorSum = 0;
        for (int i = 0; i < randLen; i++) {
            do{
                randArray.add((int) (Math.random() * 10) % 16 + 1);
            }while (i != 0 && Objects.equals(randArray.get(i), randArray.get(i - 1)));
            xorSum ^= randArray.get(i);
        }
        if (xorSum != 0){
            for (int i = 0; i < randLen; i++) {
                if (randArray.get(i) != xorSum){
                    randArray.set(i, xorSum);
                    break;
                }
            }
        }
        NimInit nimInit = new NimInit();
        nimInit.setLen(randLen);
        nimInit.setArray(randArray); // set origin array, so that front-end shows AI's move
        String randomToken = UUID.randomUUID().toString();
        nimInit.setGameToken(randomToken);
        NimStep nimStep = (request.isFirst() ? new NimStep(0, 0) : calcNext(randArray, xorSum));
        nimInit.setResult(nimStep);

        randArray.set(nimStep.getPos(), randArray.get(nimStep.getPos()) - nimStep.getPos());
        nimMapper.createIndex(randomToken, new Gson().toJson(randArray), request.isEasy());
        return nimInit;
    }

    @Override
    public NimResult step(NimRequest step){
        Map<String, Object> map = ThreadLocalUtil.get();
        List<Integer> stones = new Gson().fromJson(nimMapper.getStones(step.getGameToken()), List.class);
        NimStep userStep = step.getStep();
        if (userStep.getPos() < 0 || userStep.getPos() >= stones.size() || userStep.getNum() <= 0 || userStep.getNum() > stones.get(userStep.getPos())){
            NimResult err = new NimResult();
            err.setStep(new NimStep(-1, -1));
            return err;
        }
        // check user wins
        if (stones.stream().allMatch(i -> i == 0)){
            NimResult win = new NimResult();
            win.setStep(new NimStep(-1, -1));
            win.setPass(1);
            win.setGameToken(step.getGameToken());
            win.setWinner(map.get("username").toString());
            nimMapper.gameEnd(step.getGameToken());
            return win;
        }

        stones.set(userStep.getPos(), stones.get(userStep.getPos()) - userStep.getNum());
        int xor = 0;
        for (int stone : stones) {
            xor ^= stone;
        }
        NimStep next = calcNext(stones, xor);
        stones.set(next.getPos(), stones.get(next.getPos()) - next.getNum());
        nimMapper.updateStones(step.getGameToken(), new Gson().toJson(stones));
        NimResult result = new NimResult();
        result.setStep(next);
        result.setPass(0);
        result.setGameToken(step.getGameToken());
        return result;
    }

    private NimStep calcNext(List<Integer> stones, int xor){
        if (xor == 0){
            // 遍历所有石子堆，如果石子堆数量大于1，则随机拿走一些石子
            for (int i = 0; i < stones.size(); i++) {
                if (stones.get(i) >= 1){
                    int take = stones.get(i) == 1 ? 1 : ((int) (Math.random() * 10) % (stones.get(i) - 1) + 1);
                    return new NimStep(i, take);
                }
            }
        }
        else {
            for (int i = 0; i < stones.size(); i++) {
                if ((stones.get(i) ^ xor) < stones.get(i)){
                    int take = stones.get(i) - (stones.get(i) ^ xor);
                    return new NimStep(i, take);
                }
            }
        }
        assert false;
        return new NimStep(0, 1);
    }
}
