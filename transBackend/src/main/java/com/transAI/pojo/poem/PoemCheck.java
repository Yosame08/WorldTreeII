package com.transAI.pojo.poem;

import lombok.Data;

@Data
public class PoemCheck {
    private boolean ok;
    private String result;
    private String title;
    private String question;

    public PoemCheck(boolean ok, String result, String title, String question) {
        this.ok = ok;
        this.result = result;
        this.title = title;
        this.question = question;
    }
}
