package com.transAI.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.transAI.mapper.NimMapper;
import com.transAI.mapper.TaskMapper;
import com.transAI.pojo.nim.*;
import com.transAI.service.NimService;
import com.transAI.utils.DateLogger;
import com.transAI.utils.ThreadLocalUtil;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NimServiceImpl implements NimService {

    private final NimMapper nimMapper;
    private final TaskMapper taskMapper;
    private final TartsServiceImpl tartsServiceImpl;

    public NimServiceImpl(NimMapper nimMapper, TaskMapper taskMapper, TartsServiceImpl tartsServiceImpl) {
        this.nimMapper = nimMapper;
        this.taskMapper = taskMapper;
        this.tartsServiceImpl = tartsServiceImpl;
    }

    @Override
    public NimInit init(NimInitRequest request){
        Map<String, Object> map = ThreadLocalUtil.get();
        int randLen = (int) (Math.random() * 10) % 4 + 5;
        // generate random array with xor sum is 0
        List<Integer> randArray = new ArrayList<>();
        int xorSum = 0;
        for (int i = 0; i < randLen; i++) {
            randArray.add(0);
            do{
                randArray.set(i, (int) (Math.random() * 10) % 16 + 1);
            }while (i != 0 && Objects.equals(randArray.get(i), randArray.get(i - 1)));
            xorSum ^= randArray.get(i);
        }
        if (xorSum != 0){
            for (int i = 0; i < randLen; i++) {
                if (randArray.get(i) != xorSum){
                    randArray.set(i, randArray.get(i) ^ xorSum);
                    break;
                }
            }
        }
        Gson gson = new Gson();
        NimInit nimInit = new NimInit();
        nimInit.setLen(randLen);
        nimInit.setArray(gson.fromJson(gson.toJson(randArray), new TypeToken<List<Integer>>(){}.getType())); // set origin array, so that front-end shows AI's move
        String randomToken = UUID.randomUUID().toString();
        nimInit.setGameToken(randomToken);
        NimStep nimStep = (request.isPlayerFirst() ? new NimStep(0, 0) : calcNext(randArray, 0));
        nimInit.setResult(nimStep);

        randArray.set(nimStep.getPos(), randArray.get(nimStep.getPos()) - nimStep.getNum());
        String stringify = gson.toJson(randArray);
        nimMapper.createIndex(randomToken, stringify, request.isEasy());
        System.out.println("[" + DateLogger.getTime() + " New Nim] User " + map.get("username") + " starts a new game with stones " + randArray + ", "
                + (request.isEasy()?"Easy, ":"Impossible, ") + (request.isPlayerFirst()?"Player First":"AI First"));
        return nimInit;
    }

    @Override
    public NimResult step(NimRequest step){
        Map<String, Object> map = ThreadLocalUtil.get();
        List<Integer> stones = new Gson().fromJson(nimMapper.getStones(step.getGameToken()), new TypeToken<List<Integer>>(){}.getType());
        NimStep userStep = step.getStep();
        System.out.println("Stone remains " + stones + ", user step " + userStep);
        if (userStep.getPos() < 0 || userStep.getPos() >= stones.size() || userStep.getNum() <= 0 || userStep.getNum() > stones.get(userStep.getPos())){
            NimResult err = new NimResult();
            err.setStep(new NimStep(-1, -1));
            return err;
        }
        stones.set(userStep.getPos(), stones.get(userStep.getPos()) - userStep.getNum());
        // check user wins
        if (stones.stream().allMatch(i -> i == 0)){
            NimResult win = new NimResult();
            win.setStep(new NimStep(0, 0));
            win.setPass(1);
            win.setWinner(map.get("username").toString());
            // get if it is easy mode
            if (nimMapper.getEasy(step.getGameToken())){
                tartsServiceImpl.passPartialTask((int)map.get("id"), 15, 20, false);
            }
            else {
                tartsServiceImpl.passTask(15, true);
            }
            nimMapper.gameEnd(step.getGameToken());
            return win;
        }
        // check AI wins
        int nonZero = 0, nonZeroPos = 0;
        for (int i = 0; i < stones.size(); i++) {
            if (stones.get(i) == 0)continue;
            nonZero++;
            nonZeroPos = i;
        }
        if (nonZero == 1){
            NimResult win = new NimResult();
            win.setStep(new NimStep(nonZeroPos, stones.get(nonZeroPos)));
            win.setPass(1);
            win.setWinner("对手");
            nimMapper.gameEnd(step.getGameToken());
            return win;
        }
        // competition continues
        int xor = 0;
        for (int stone : stones) {
            xor ^= stone;
        }
        NimStep next = nimMapper.getEasy(step.getGameToken()) ? randNext(stones) : calcNext(stones, xor);
        stones.set(next.getPos(), stones.get(next.getPos()) - next.getNum());
        nimMapper.updateStones(step.getGameToken(), new Gson().toJson(stones));
        NimResult result = new NimResult();
        result.setStep(next);
        result.setPass(0);
        System.out.println("[" + DateLogger.getTime() + " Nim Step] User " + map.get("username") + " moves " + userStep.getNum() +
                " stone at pos " + userStep.getPos() + ", AI moves " + next.getNum() + " stone at pos " + next.getPos());
        return result;
    }

    private NimStep calcNext(List<Integer> stones, int xor){
        System.out.println("AI xor " + xor);
        if (xor == 0) return randNext(stones);
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

    private NimStep randNext(List<Integer> stones){
        int pos = (int) (Math.random() * 10) % stones.size();
        while (stones.get(pos) == 0){
            pos = (int) (Math.random() * 10) % stones.size();
        }
        int take = stones.get(pos) == 1 ? 1 : ((int) (Math.random() * 10) % (stones.get(pos) - 1) + 1);
        return new NimStep(pos, take);
    }
}
