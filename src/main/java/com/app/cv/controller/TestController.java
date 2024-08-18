package com.app.cv.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/ams/test")
    public String test() {
        return "Admin service is working!";
    }
}
