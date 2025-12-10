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

    // ======================================
    // CU07 - Actualizar factura (ID dentro del DTO)
    // ======================================
    @PutMapping
    public ResponseEntity<String> actualizarFactura(@RequestBody FacturaDTO facturaDTO) {
        facturaService.actualizarFactura(facturaDTO);
        return ResponseEntity.ok("Factura actualizada correctamente.");
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

}
