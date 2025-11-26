package com.hotelPremier.controller;

import com.hotelPremier.classes.mapper.ClassMapper;
import com.hotelPremier.service.HabitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HabitacionController {
    @Autowired
    ClassMapper classMapper;

    @Autowired
    HabitacionService habitacionService;


}
