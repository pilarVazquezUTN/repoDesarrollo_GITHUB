package com.hotelPremier.service;

import com.hotelPremier.classes.reserva.Reserva;
import com.hotelPremier.repository.HuespedRepositoryDAO;
import com.hotelPremier.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaServiceImpl implements ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Override
    public List<Reserva> findByApellidoContainingIgnoreCase(String apellido) {
        return List.of();
    }
}
