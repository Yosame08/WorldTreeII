package com.transAI.pojo.nim;

import lombok.Data;

@Data
public class NimResult {

    private NimStep step;
    private int pass;
    private String gameToken;
    private String winner;

}
