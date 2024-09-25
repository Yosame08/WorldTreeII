package com.transAI.pojo;

import lombok.Data;

@Data
public class Pos {
    private int level;
    private double lng;
    private double lat;

    public Pos(int i, int i1, int i2) {
        level = i;
        lng = i1;
        lat = i2;
    }
}
