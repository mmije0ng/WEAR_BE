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

    @GetMapping("api/products/new/{userId}")
    public String testProuct(@PathVariable("userId") Long id){
        return "api/products/new/"+id;
    }

   /* @PostMapping("api/products/new/{userId}")
    public String testProuctPost(@PathVariable("userId") Long id){
        return "api/products/new/"+id;
    }*/

//    @GetMapping("api/users/profile/{userId}")
//    public String testGetprofile(@PathVariable("userId") Long id){
//        return "GET: api/users/profile/"+id;
//    }
//
//    @PostMapping("api/users/profile/{userId}")
//    public String testPostProfile(@PathVariable("userId") Long id){
//        return "POST: api/users/profile/"+id;
//    }
}