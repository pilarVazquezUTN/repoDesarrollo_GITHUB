package com.hotelPremier.controller;

import com.hotelPremier.service.EstadiaService;
import com.hotelPremier.classes.DTO.EstadiaDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estadias")
public class EstadiaController {

    @Autowired
    private EstadiaService estadiaService;

    // ============================================
    // GET /api/estadias/sin-factura
    // ============================================
    @GetMapping("/sin-factura")
    public List<EstadiaDTO> listarSinFactura() {
        return estadiaService.obtenerEstadiasSinFactura();
    }

    @GetMapping("/enCurso/{numHabitacion}")
    public ResponseEntity<?> obtenerEnCurso(@PathVariable Integer numHabitacion) {

        EstadiaDTO dto = estadiaService.obtenerEstadiaEnCurso(numHabitacion);

        if (dto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No hay estadía ENCURSO para la habitación " + numHabitacion);
        }

        return ResponseEntity.ok(dto);
    }
}
