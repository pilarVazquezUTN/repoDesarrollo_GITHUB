package com.hotelPremier.controller;

import com.hotelPremier.classes.huesped.Huesped;
import com.hotelPremier.classes.huesped.HuespedDTO;
import com.hotelPremier.classes.habitacion.*;
import com.hotelPremier.classes.mapper.ClassMapper;
import com.hotelPremier.service.HabitacionService;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
        /*
        switch segun el tipo de habitacion que me entre y de ahi llamo a cada controller.
        */
        
        List<HabitacionDTO> listaHabitaciones = habitacionService.getHabitaciones(tipo);
        return new ResponseEntity<>(listaHabitaciones, HttpStatus.OK);
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
    public ResponseEntity<List<HabitacionDTO>> obtenerHabitacionesConDetalle(){
        List<HabitacionDTO> listaHabitaciones = habitacionService.obtenerHabitacionesConDetalle();
        return new ResponseEntity<>(listaHabitaciones, HttpStatus.OK);
    }


    @GetMapping("/{nro}/huespedes")
    public List<Huesped> getHuespedesDeHabitacion(@PathVariable Integer nro) {
        return habitacionService.obtenerHuespedesPorHabitacion(nro);
    }

}
