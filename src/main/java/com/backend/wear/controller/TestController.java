package com.backend.wear.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class TestController {
    //
    @RequestMapping()
    @GetMapping()
    public String main(){
        return "Hello World";
    }
}