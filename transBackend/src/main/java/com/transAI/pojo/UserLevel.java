package com.transAI.pojo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserLevel {
    @NotNull
    private Integer id;
    private Integer levelId;
}