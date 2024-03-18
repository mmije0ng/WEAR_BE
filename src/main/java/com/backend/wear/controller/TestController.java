package com.backend.wear.controller;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/")
public class TestController {
    //
    @RequestMapping()
    @GetMapping()
    public String main(){
        return "Hello World";
    }

    @GetMapping("users/{userId}")
    public String testControllerMethod(@PathVariable("userId") Long id){
        return "userId: "+id;
    }

    @GetMapping("api/products/new/{userId}")
    public String testProuct(@PathVariable("userId") Long id){
        return "api/products/new/"+id;
    }

    @PostMapping("api/products/new/{userId}")
    public String testProuctPost(@PathVariable("userId") Long id){
        return "Post: api/products/new/"+id;
    }
}