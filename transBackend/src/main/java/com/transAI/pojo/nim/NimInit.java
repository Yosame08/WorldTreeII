package com.transAI.pojo.nim;

import lombok.Data;

import java.util.List;

@Data
public class NimInit {

    private int len;
    private String gameToken;
    private List<Integer> array;
    private NimStep result; // the number when AI takes first

}
