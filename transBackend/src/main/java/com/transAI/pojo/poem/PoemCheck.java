package com.transAI.pojo.poem;

import lombok.Data;

@Data
public class PoemCheck {
    private boolean ok;
    private String result;

    public PoemCheck(boolean ok, String result) {
        this.ok = ok;
        this.result = result;
    }
}
