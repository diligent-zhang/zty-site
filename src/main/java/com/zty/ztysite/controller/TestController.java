package com.zty.ztysite.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Test")
public class TestController {
    @PostMapping("/test")
    public String test() {
        return "test";
    }


}
