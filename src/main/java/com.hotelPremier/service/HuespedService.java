package com.hotelPremier.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import Classes.Huesped.Huesped;

//@Service
public interface HuespedService {
    List<Huesped> findAll();
    List<Huesped> findByCategory(String DNI);
} 
