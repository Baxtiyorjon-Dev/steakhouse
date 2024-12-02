package com.steakhouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.ControllerAdvice;

@SpringBootApplication
@ControllerAdvice
public class SteakhouseApplication {

    public static void main(String[] args) {
        SpringApplication.run(SteakhouseApplication.class, args);
    }

}
