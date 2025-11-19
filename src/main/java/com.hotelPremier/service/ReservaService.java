package com.hotelPremier.service;

import java.util.List;

public interface ReservaService {
    List<Classes.Reserva.Reserva> findByApellidoContainingIgnoreCase(String apellido);
}
