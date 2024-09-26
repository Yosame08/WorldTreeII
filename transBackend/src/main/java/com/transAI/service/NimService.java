package com.transAI.service;

import com.transAI.pojo.nim.*;

public interface NimService {

    NimInit init(NimInitRequest request);
    NimResult step(NimRequest step);

}
