package com.fdalg.worldtree_front.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RankController {
    @GetMapping("/rank")
    public String rank() {
        return "Hello Rank!";
    }
}
