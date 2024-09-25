package com.transAI.pojo;

import lombok.Data;

@Data
public class UserPair {

    private String usernameA;
    private String usernameB;

    public UserPair(String usernameA, String usernameB) {
        this.usernameA = usernameA;
        this.usernameB = usernameB;
    }
}
