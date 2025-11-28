package com.hotelPremier.controller;

import com.hotelPremier.service.ReservaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotelPremier.classes.mapper.ClassMapper;
import com.hotelPremier.classes.reserva.ReservaDTO;

import java.sql.Date;
import java.util.List;

@RestController
//@CrossOrigin(origins = "http://localhost:3000")
public class ReservaController {

    @Autowired
    ClassMapper classMapper;

    @Autowired
    ReservaService reservaService;


    /**
     * Endpoint para obtener la lista de reservas.
     * Mapea a: GET http://localhost:8080/api/reservas?apellido=Perez
     */

    @GetMapping ("/reservas")
    public ResponseEntity<List<ReservaDTO>> getReservas(        
        @RequestParam Date fechaDesde,
        @RequestParam Date fechaHasta
    ){
        List<ReservaDTO> listaReservas = reservaService.getReservas(fechaDesde,fechaHasta);
        return new ResponseEntity<>(listaReservas, HttpStatus.OK);
    }

    @PostMapping("/reservas")
    public ResponseEntity<ReservaDTO> crearReserva(@RequestBody ReservaDTO reservaDTO) {

        ReservaDTO creada = reservaService.crearReserva(reservaDTO);
        return ResponseEntity.ok(creada);
    }








}

