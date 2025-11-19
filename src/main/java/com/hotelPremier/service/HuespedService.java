package com.hotelPremier.service;

import java.util.List;

import com.hotelPremier.classes.huesped.Huesped;

//@Service
public interface HuespedService {
    List<Huesped> findAll();
    List<Huesped> findByCategory(String DNI);

    Huesped addHuesped(Huesped huesped);
    void deleteHuesped(long DNI);
}
