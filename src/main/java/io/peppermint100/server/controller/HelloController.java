package io.peppermint100.server.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping("/")
    public String hello(){
        return "Hello";
    }

    @GetMapping("/hello/{key}")
    public String HelloKey(@PathVariable("key") String key){
        return "Hello" + key;
    }
}
