package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
//@ServletComponentScan(value = {"com.example.filter", "com.example.listener"})
public class Application {

    public static void main(String[] args) throws Exception {
        log.info("main doing...");
        SpringApplication.run(Application.class, args);
        log.info("main done");
    }

}