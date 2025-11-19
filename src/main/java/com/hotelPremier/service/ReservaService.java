package com.hotelPremier.service;

import java.util.List;

import com.hotelPremier.classes.reserva.Reserva;

public interface ReservaService {
    List<Reserva> findByApellidoContainingIgnoreCase(String apellido);
}
