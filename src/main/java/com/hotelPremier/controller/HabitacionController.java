package com.hotelPremier.controller;

import com.hotelPremier.classes.DTO.HabitacionDTO;
import com.hotelPremier.classes.Dominio.Huesped;
import com.hotelPremier.classes.mapper.ClassMapper;
import com.hotelPremier.service.HabitacionService;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HabitacionController {
    @Autowired
    ClassMapper classMapper;

    @Autowired
    HabitacionService habitacionService;

    @GetMapping("/habitaciones")
    public ResponseEntity<List<HabitacionDTO>> getHabitaciones(
        @RequestParam String tipo
    ){
        List<HabitacionDTO> listaHabitaciones = habitacionService.getHabitaciones(tipo);
        return new ResponseEntity<>(listaHabitaciones, HttpStatus.OK);
    }

    @GetMapping("/numHabitacion")
    public HabitacionDTO getHabitacion(
        @RequestParam Integer numero
    ){
        HabitacionDTO habitacionDTO = habitacionService.getHabitacion(numero);
        return (habitacionDTO);
    }

    @GetMapping("/listaHabitaciones") 
    public ResponseEntity<List<HabitacionDTO>> buscarListaHabitaciones(
        @RequestParam String tipo,
        @RequestParam Date fechaDesde,
        @RequestParam Date fechaHasta
    ){
        
        List<HabitacionDTO> listaHabitaciones = habitacionService.buscarListaHabitaciones(tipo,fechaDesde,fechaHasta);
        return new ResponseEntity<>(listaHabitaciones, HttpStatus.OK);
    }

    @GetMapping("/detalleHabitaciones")
    public ResponseEntity<List<HabitacionDTO>> obtenerHabitacionesConDetalle(
        @RequestParam String tipo,
        @RequestParam Date fechaDesde,
        @RequestParam Date fechaHasta
    ){
        List<HabitacionDTO> listaHabitaciones = habitacionService.obtenerHabitacionesConDetalle(tipo,fechaDesde,fechaHasta);
        return new ResponseEntity<>(listaHabitaciones, HttpStatus.OK);
    }


    @GetMapping("/{nro}/huespedes")
    public List<Huesped> getHuespedesDeHabitacion(@PathVariable Integer nro) {
        return habitacionService.obtenerHuespedesPorHabitacion(nro);
    }

}
