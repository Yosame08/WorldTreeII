package com.transAI.service.impl;

import com.transAI.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UtilServiceImpl implements UtilService {


    @Override
    public LocalDateTime getNowTime() {
        return LocalDateTime.now();
    }
}
