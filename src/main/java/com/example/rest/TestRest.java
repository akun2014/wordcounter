package com.example.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by akun on 2017/5/7.
 */
@Slf4j
@RestController
@RequestMapping("/rest/test")
public class TestRest {


    @RequestMapping("/index")
    String home() {
        return "Hello World!";
    }
}
