package com.transAI.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserTotalPoint {
    private Integer userId;
    private Integer point;
    private LocalDateTime time;
}
