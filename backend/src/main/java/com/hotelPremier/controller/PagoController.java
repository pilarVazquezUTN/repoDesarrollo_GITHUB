package com.hotelPremier.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotelPremier.classes.DTO.PagoDTO;
import com.hotelPremier.service.PagoService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/pagos")
@Tag(name = "Pagos", description = "Registro de pagos de facturas")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @Operation(summary = "Registrar pago")
    @PostMapping
    public ResponseEntity<String> registrar(@RequestBody PagoDTO dto) {
        return ResponseEntity.ok(pagoService.ingresarPago(dto));
    }
}
