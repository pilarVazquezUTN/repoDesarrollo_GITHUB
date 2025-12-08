package com.hotelPremier.controller;

import com.hotelPremier.service.ReservaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotelPremier.classes.DTO.HabitacionDTO;
import com.hotelPremier.classes.DTO.ReservaDTO;
import com.hotelPremier.classes.mapper.ClassMapper;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
//@CrossOrigin(origins = "http://localhost:3000")
public class ReservaController {

    @Autowired
    ClassMapper classMapper;

    @Autowired
    ReservaService reservaService;


    /**
     * Endpoint para obtener la lista de reservas.
     * Mapea a: GET http://localhost:8080/api/reservas?apellido=    
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
    public ResponseEntity<String> crearReserva(@RequestBody List<ReservaDTO> listareservaDTO) {

        List<ReservaDTO> listaReservasguardadas = reservaService.crearReserva(listareservaDTO);
        return ResponseEntity.ok("Reservas registradas con exito");
    }

    /**
     * recibe la fecha desde, hasta , el listado de habitacionDTO
     * @param fechaDesde
     * @param fechaHasta
     * @param habitacionDTOS
     * @return
     */

    @PostMapping("/listados")
    public ResponseEntity<List<Map<String, Object>>> generarListado(
            @RequestParam LocalDate fechaDesde,
            @RequestParam LocalDate fechaHasta,
            @RequestBody List<HabitacionDTO> habitacionDTOS) {

        List<Map<String, Object>> listado = reservaService.generarListadoReservar(fechaDesde, fechaHasta, habitacionDTOS);

        return ResponseEntity.ok(listado);
    }







}

