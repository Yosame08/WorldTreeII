package com.transAI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableScheduling
public class transAIApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(transAIApplication.class, args);
    }
}
