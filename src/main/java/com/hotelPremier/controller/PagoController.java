package com.hotelPremier.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotelPremier.classes.DTO.PagoDTO;
import com.hotelPremier.service.PagoService;

@RestController
@RequestMapping("/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @PostMapping
    public ResponseEntity<String> registrar(@RequestBody PagoDTO dto) {
        return ResponseEntity.ok(pagoService.ingresarPago(dto));
    }
}
