package com.transAI.pojo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Task {

    @NotNull
    private Integer taskId;
    private String taskTitle;
    private double taskPosX;
    private double taskPosY;
    private String taskDescription;
    private String taskDescriptionFull;
    private String uri;
    private boolean submission;
    private Integer taskPoint;
    private Integer getPoint;
    private Integer taskCoin;
    private Integer hintPrice;
    private Integer taskStatus;
    private Integer hintStatus;
}
