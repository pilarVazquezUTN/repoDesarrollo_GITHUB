package com.hotelPremier.controller;

import com.hotelPremier.classes.DTO.FacturaDTO;
import com.hotelPremier.service.FacturaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    // ======================================
    // CU07 - Obtener facturas por habitación
    // ======================================
    @GetMapping("/habitacion/{nro}")
    public ResponseEntity<List<FacturaDTO>> obtenerPorHabitacion(@PathVariable Integer nro) {
        return ResponseEntity.ok(facturaService.obtenerFacturasPorHabitacion(nro));
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<List<FacturaDTO>> obtenerPorDni(@PathVariable String dni) {
        return ResponseEntity.ok(facturaService.buscarPorDni(dni));
    }

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
    @PostMapping
    public ResponseEntity<?> crearFactura(@RequestBody FacturaDTO facturaDTO) {
        try {
            FacturaDTO creada = facturaService.crearFactura(facturaDTO);
            System.err.println(creada);
            return ResponseEntity.ok("Factura creada correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
