package com.hotelPremier.controller;

import java.util.List;

import com.hotelPremier.service.HuespedService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotelPremier.classes.DTO.HuespedDTO;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@Tag(name = "Huéspedes", description = "ABM y búsqueda de huéspedes")
public class HuespedController {

    @Autowired 
    private HuespedService huespedService;

    @Operation(summary = "Buscar huéspedes")
    @GetMapping("/huespedes")
    public ResponseEntity<List<HuespedDTO>> getHuespedes(
        @RequestParam(required = false) String dni,
        @RequestParam(required = false) String nombre,
        @RequestParam(required = false) String apellido,
        @RequestParam(required = false) String tipoDocumento
    ) {
        return ResponseEntity.ok(
            huespedService.buscarHuespedes(dni, nombre, apellido, tipoDocumento)
        );
    }

    // @Operation(summary = "Dar de alta huésped")
    // @PostMapping("/huespedes")
    // public ResponseEntity<HuespedDTO> addHuesped(@RequestBody HuespedDTO huesped) {
    //     return ResponseEntity.ok(huespedService.addHuesped(huesped));
    // }

    @Operation(summary = "Eliminar huésped")
    @DeleteMapping("/huespedes/{tipo}/{dni}")
    public ResponseEntity<Void> deleteHuesped(
        @PathVariable String tipo,
        @PathVariable String dni
    ) {
        huespedService.deleteHuesped(tipo, dni);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Modificar huésped")
    @PutMapping("/huespedes/modificar")
    public ResponseEntity<HuespedDTO> updateHuesped(
        @RequestBody HuespedDTO huespedDTO
    ) {
        return ResponseEntity.ok(huespedService.updateHuesped(huespedDTO));
    }
}
