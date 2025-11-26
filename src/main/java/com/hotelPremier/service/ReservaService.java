package com.hotelPremier.service;

import java.sql.Date;
import java.util.List;

import com.hotelPremier.classes.habitacion.Habitacion;
import com.hotelPremier.classes.reserva.Reserva;
import com.hotelPremier.repository.HabitacionRepository;
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
    private HabitacionRepository habitacionRepository;
    @Autowired
    private ClassMapper mapper;

    public List<ReservaDTO> getReservas(
        Date fechaDesde,
        Date fechaHasta
    ){
        return mapper.toDtosReserva( reservaRepository.buscarReservas(fechaDesde,fechaHasta) );
    }

    public ReservaDTO crearReserva(ReservaDTO reservaDTO){

        //aca convierto la reserva dto en reserva para mandarle a la bdd
        Reserva reserva = mapper.toEntityReserva(reservaDTO);



        //AGREGO ESTO PARA QUE BUSQUE EL NUMERO DE HABITACION DE LA BASE DE DATOS Y HAGA EL SET
        Habitacion hab = habitacionRepository
                .findById(reservaDTO.getNro_habitacion())
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada"));

        // Asignarla a la reserva
        reserva.setHabitacion(hab);

        // guardar en BD
        Reserva guardada = reservaRepository.save(reserva);


        return mapper.toDTOReserva(guardada);
    }
}
