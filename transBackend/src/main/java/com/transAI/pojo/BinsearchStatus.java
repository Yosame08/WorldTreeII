package com.transAI.pojo;

import lombok.Data;

@Data
public class BinsearchStatus {

    private boolean canSubmit;
    private Integer chance;

    public BinsearchStatus(boolean canSubmit, Integer chance) {
        this.canSubmit = canSubmit;
        this.chance = chance;
    }

}
