package com.hotelPremier.controller;

import com.hotelPremier.classes.DTO.NotaDeCreditoDTO;
import com.hotelPremier.service.NotaDeCreditoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notascredito")
public class NotaDeCreditoController {

    @Autowired
    private NotaDeCreditoService service;

    @PostMapping
    public ResponseEntity<?> ingresarNotaCredito(@RequestBody NotaDeCreditoDTO dto) {
        try {
            String mensaje = service.ingresarNotaDeCredito(dto);
            return ResponseEntity.ok(mensaje);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
