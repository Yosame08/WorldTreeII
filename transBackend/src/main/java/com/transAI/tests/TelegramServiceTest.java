package com.transAI.tests;

import com.transAI.pojo.UserPair;
import com.transAI.service.TelegramService;
import jakarta.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TelegramServiceTest {
    @Resource
    private TelegramService XXXService;
    @Test
    public void conflictTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse("2020-10-26", dtf);
        LocalDate end = LocalDate.parse("2020-10-31", dtf);
        System.out.println(XXXService.getPlayed(new UserPair("oneton", "Kiriko")));
        System.out.println(XXXService.getPlayed(new UserPair("oneton", "Kiriko")));
        System.out.println(XXXService.getPlayed(new UserPair("oneton", "Kiriko")));
        System.out.println(XXXService.getPlayed(new UserPair("oneton", "Kiriko")));
        System.out.println(XXXService.getPlayed(new UserPair("oneton", "Kiriko")));
        System.out.println(XXXService.getPlayed(new UserPair("oneton", "Kiriko")));
        System.out.println(XXXService.getPlayed(new UserPair("oneton", "Kiriko")));
    }
}