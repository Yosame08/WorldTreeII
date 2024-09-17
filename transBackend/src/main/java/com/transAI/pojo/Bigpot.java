package com.transAI.pojo;

import lombok.Data;

import java.util.List;

@Data
public class Bigpot {

    private Integer level;
    private String gameToken;
    private Integer len;
    private Integer x;
    private Integer y;
    private Integer z;
    private Integer w;

    public Bigpot(int i, String gameToken1, int len, int i1, int i2, int i3, int i4) {
        this.level = i;
        this.gameToken = gameToken1;
        this.len = len;
        this.x = i1;
        this.y = i2;
        this.z = i3;
        this.w = i4;
    }
}
