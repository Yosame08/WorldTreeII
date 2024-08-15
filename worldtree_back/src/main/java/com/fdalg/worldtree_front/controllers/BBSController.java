package com.fdalg.worldtree_front.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BBSController {
    @GetMapping("/bbs")
    public String bbs() {
        return "Hello BBS!";
    }
}
