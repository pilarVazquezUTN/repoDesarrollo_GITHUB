package com.hotelPremier.service;

import java.util.List;

import com.hotelPremier.classes.huesped.HuespedDTO;


public interface HuespedService {
    List<HuespedDTO> findAll();
    List<HuespedDTO> findByCategory(String DNI);

    HuespedDTO addHuesped(HuespedDTO huespedDTO);
    void deleteHuesped(long DNI);
}
