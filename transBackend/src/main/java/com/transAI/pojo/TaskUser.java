package com.transAI.pojo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskUser {

    @NotNull
    private Integer taskId;
    @NotNull
    private Integer userId;

    private Integer status;
    private Integer point;

    private LocalDateTime time;
}
