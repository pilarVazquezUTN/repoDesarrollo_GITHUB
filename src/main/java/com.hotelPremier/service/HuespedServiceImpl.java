package com.hotelPremier.service;//package com.hotelPremier.service;

import com.hotelPremier.repository.HuespedRepository;
import org.springframework.stereotype.Service;

import Classes.Huesped.Huesped;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;


@Service
public class HuespedServiceImpl implements HuespedService {

    @Autowired
    private HuespedRepository huespedRepository;

    @Override
    public List<Huesped> findAll(){

    }

    @Override
    public List<Huesped> findByCategory(String DNI) {
        //return List.of();
    }

}
