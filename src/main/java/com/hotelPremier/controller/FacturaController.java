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
        description = "Permite filtrar facturas por CUIT, tipo de documento y número"
    )
    @GetMapping("/filtrar")
    public ResponseEntity<List<FacturaDTO>> filtrarFacturas(
        @RequestParam(required = false) String cuit,
        @RequestParam(required = false) String tipo,
        @RequestParam(required = false) String numero
    ) {
        return ResponseEntity.ok(
                facturaService.filtrarFacturas(cuit, tipo, numero)
        );
    }

    @Operation(
        summary = "Crear factura",
        description = "Genera una nueva factura asociada a una estadía"
    )
    @PostMapping
    public ResponseEntity<?> crearFactura(@RequestBody FacturaDTO facturaDTO) {
        try {
            facturaService.crearFactura(facturaDTO);
            return ResponseEntity.ok("Factura creada correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
