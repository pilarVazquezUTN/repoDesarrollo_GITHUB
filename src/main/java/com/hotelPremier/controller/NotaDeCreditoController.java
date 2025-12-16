package com.hotelPremier.controller;

import com.hotelPremier.classes.DTO.NotaDeCreditoDTO;
import com.hotelPremier.service.NotaDeCreditoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/notadecredito")
@Tag(name = "Notas de Crédito", description = "Gestión de notas de crédito")
public class NotaDeCreditoController {

    @Autowired
    private NotaDeCreditoService service;

    @Operation(
        summary = "Ingresar nota de crédito",
        description = "Genera una nota de crédito que anula contablemente una o más facturas"
    )
    @PostMapping
    public ResponseEntity<?> ingresarNotaCredito(@RequestBody NotaDeCreditoDTO dto) {
        try {
            return ResponseEntity.ok(service.ingresarNotaDeCredito(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
