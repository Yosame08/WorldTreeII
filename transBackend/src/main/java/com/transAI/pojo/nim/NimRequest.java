package com.transAI.pojo.nim;

import lombok.Data;

@Data
public class NimRequest {

    private String gameToken;
    private NimStep step;

}
