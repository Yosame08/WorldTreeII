package com.transAI.pojo.nim;

import lombok.Data;

@Data
public class NimStep {

    private int pos;
    private int num;

    public NimStep(int i, int take) {
        this.pos = i;
        this.num = take;
    }
}
