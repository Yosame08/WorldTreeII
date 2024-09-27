package com.transAI.service;

import com.transAI.pojo.poem.PoemCheck;

public interface PoemService {
    PoemCheck getPoem(Integer times);
    PoemCheck submitAnswer(String answer);
    Boolean next();
}
