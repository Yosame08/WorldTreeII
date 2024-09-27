package com.transAI.pojo.bigpot;

import lombok.Data;

import java.util.List;

@Data
public class Bigpot2 {

    private String gameToken;
    private Integer level;
    private List<Integer> numbers;

    public Bigpot2(String gameToken1, int level, List<Integer> list) {
        this.gameToken = gameToken1;
        this.level = level;
        this.numbers = list;
    }
}
