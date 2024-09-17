package com.transAI.service.impl;

import com.transAI.mapper.BigpotMapper;
import com.transAI.pojo.Bigpot;
import com.transAI.service.BigpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BigpotServiceImpl implements BigpotService {

    @Autowired
    private BigpotMapper bigpotMapper;
    @Override
    public Bigpot init(int id) {
        int level = bigpotMapper.getLevel(id);

        Bigpot bigpot = getBigpot(level);

        return bigpot;
    }


    private Bigpot getBigpot(int level) {
        Bigpot bigpot = null;
        String randomToken = UUID.randomUUID().toString();
        switch(level) {
            case 1:
                bigpot = new Bigpot(1, randomToken, 4, 1, 2, 3, 4);
                break;
            case 2:
                bigpot = new Bigpot(1, randomToken, 4, 2, 3, 4, 5);
                break;
            case 3:
                bigpot = new Bigpot(1, randomToken, 4, 3, 4, 5, 6);
                break;
            case 4:
                bigpot =new  Bigpot(1, randomToken, 4, 4, 5, 6, 7);
                break;
            default:
                return null; // 或者你可以抛出异常
        }
        bigpotMapper.insert(bigpot);
        return bigpot;
    }

}
