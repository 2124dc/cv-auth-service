package com.app.cv.controller;


import com.app.cv.service.AuthDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    AuthDetailsService authService;

    @GetMapping("/auth/test")
    public String test() {
        return "Admin service is working!";
    }
}
