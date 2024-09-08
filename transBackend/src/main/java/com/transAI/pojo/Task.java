package com.transAI.pojo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Task {

    @NotNull
    private Integer taskId;
    private String taskTitle;
    private Integer taskPosX;
    private Integer taskPosY;
    private String taskDescription;
    private String uri;
    private Integer taskPoint;
    private Integer taskCoin;
    private Integer hintPrice;
}
