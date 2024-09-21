package com.transAI.pojo;

import lombok.Data;

import java.util.List;

@Data
public class Bigpot {

    private String gameToken;
    private Integer level;
    private Integer len;
    private Integer x;
    private Integer y;
    private Integer z;
    private Integer w;

    public Bigpot(String gameToken1, int level, int len, int i1, int i2, int i3, int i4) {
        this.gameToken = gameToken1;
        this.level = level;
        this.len = len;
        this.x = i1;
        this.y = i2;
        this.z = i3;
        this.w = i4;
    }
}
