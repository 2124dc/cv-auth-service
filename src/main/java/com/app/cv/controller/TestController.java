package com.app.cv.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/cv-auth/test")
    public String test() {
        return "Auth Service is working!";
    }
}