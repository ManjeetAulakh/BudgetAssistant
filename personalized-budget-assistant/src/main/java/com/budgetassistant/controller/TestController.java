package com.budgetassistant.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/secure")
    public String securedTest() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return "Hello, " + username + "! You are authenticated.";
    }
}