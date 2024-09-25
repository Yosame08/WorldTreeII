package com.transAI.service;

import com.transAI.pojo.Orientation;
import com.transAI.pojo.Pos;

import java.util.List;

public interface OrientationService {
    List<Orientation> initOrientation();
    Orientation checkOrientation(Pos pos);
}
