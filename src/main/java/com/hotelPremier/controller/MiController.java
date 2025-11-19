package com.hotelPremier.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 

@RequestMapping("/api")
public class MiController {

    @GetMapping("/hola")
    public String hola(){
        return "Hola";
    }
}
