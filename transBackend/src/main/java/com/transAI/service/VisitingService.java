package com.transAI.service;

import com.transAI.pojo.Visiting;

public interface VisitingService {
    void update(Visiting visiting, int version);

    Visiting getInfo(int version);

    public void expireAndJudge(int version);
}
