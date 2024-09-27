package com.transAI.pojo.bigpot;

import lombok.Data;

@Data
public class BigpotResult {

    private int pass;
    private int result;

    public BigpotResult() {
    }

    public BigpotResult(int i, int i1) {
        pass = i;
        result = i1;
    }
}
