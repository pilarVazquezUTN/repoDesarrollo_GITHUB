package com.hotelPremier.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hotelPremier.classes.mapper.ClassMapper;
import com.hotelPremier.repository.ReservaRepository;

import com.hotelPremier.classes.reserva.ReservaDTO;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ClassMapper mapper;

    public List<ReservaDTO> getReservas(
        Date fechaDesde,
        Date fechaHasta
    ){
        return mapper.toDtosReserva( reservaRepository.buscarReservas(fechaDesde,fechaHasta) );
    }
}
