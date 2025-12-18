package com.hotelPremier.controller;

import com.hotelPremier.classes.DTO.FacturaDTO;
import com.hotelPremier.service.FacturaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/facturas")
@Tag(name = "Facturas", description = "Gestión de facturación y consultas de facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @Operation(
        summary = "Obtener facturas por habitación",
        description = "Devuelve todas las facturas asociadas a una habitación"
    )
    @GetMapping("/habitacion/{nro}")
    public ResponseEntity<List<FacturaDTO>> obtenerPorHabitacion(
        @Parameter(description = "Número de habitación", example = "101")
        @PathVariable Integer nro
    ) {
        return ResponseEntity.ok(facturaService.obtenerFacturasPorHabitacion(nro));
    }

    // @Operation(
    //     summary = "Buscar facturas por DNI",
    //     description = "Devuelve las facturas asociadas a un huésped según su DNI"
    // )
    // @GetMapping("/dni/{dni}")
    // public ResponseEntity<List<FacturaDTO>> obtenerPorDni(
    //     @Parameter(description = "DNI del huésped", example = "40123456")
    //     @PathVariable String dni
    // ) {
    //     return ResponseEntity.ok(facturaService.buscarPorDni(dni));
    // }

    @Operation(
        summary = "Filtrar facturas",
        description = "Permite filtrar facturas por CUIT, tipo de documento o número de documento. " +
                     "Se puede usar cualquiera de estos campos de forma independiente o combinada. " +
                     "Si solo se proporciona el número de documento, busca por DNI."
    )
    @GetMapping("/filtrar")
    public ResponseEntity<List<FacturaDTO>> filtrarFacturas(
        @Parameter(description = "CUIT para buscar facturas de PersonaJuridica", example = "20-12345678-9")
        @RequestParam(required = false) String cuit,
        @Parameter(description = "Tipo de documento (DNI, LC, LE, etc.)", example = "DNI")
        @RequestParam(required = false) String tipo,
        @Parameter(description = "Número de documento (DNI)", example = "40123456")
        @RequestParam(required = false) String numero
    ) {
        return ResponseEntity.ok(
                facturaService.filtrarFacturas(cuit, tipo, numero)
        );
    }

    @Operation(
        summary = "Crear factura",
        description = "Genera una nueva factura asociada a una estadía con todos los cálculos realizados automáticamente. Devuelve la factura completa creada."
    )
    @PostMapping
    public ResponseEntity<FacturaDTO> crearFactura(@RequestBody FacturaDTO facturaDTO) {
        FacturaDTO facturaCreada = facturaService.crearFactura(facturaDTO);
        return ResponseEntity.ok(facturaCreada);
    }
}
