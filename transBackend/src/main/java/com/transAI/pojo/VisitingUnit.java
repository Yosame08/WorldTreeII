package com.transAI.pojo;

import lombok.Data;

@Data
public class VisitingUnit {
    public int userId;
    public int visitingId;
    public double x;
    public double y;
    public boolean indoor;
    public int floor;
}
