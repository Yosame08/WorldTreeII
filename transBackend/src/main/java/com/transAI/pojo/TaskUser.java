package com.transAI.pojo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskUser {

    @NotNull
    private Integer taskId;
    @NotNull
    private Integer userId;

    private Integer status;
    private Integer point;
}
