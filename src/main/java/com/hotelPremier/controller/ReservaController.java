package com.hotelPremier.controller;

import com.hotelPremier.classes.DTO.ReservaDTO;
import com.hotelPremier.service.ReservaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;


import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;


@RestController
@RequestMapping("/reservas")
@Tag(name = "Reservas", description = "Gestión de reservas de habitaciones")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Operation(summary = "Guardar reservas")
    @PostMapping
    public ResponseEntity<?> guardarReservas(@RequestBody List<ReservaDTO> listaDTO) {
        return ResponseEntity.ok(reservaService.guardarLista(listaDTO));
    }

    @Operation(summary = "Buscar reservas entre fechas")
    @GetMapping
    public ResponseEntity<?> buscarEntreFechas(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta
    ) {
        return ResponseEntity.ok(
            reservaService.buscarEntreFechas(desde, hasta)
        );
    }


    @Operation(summary = "Buscar reservas por apellido y nombre")
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPorApellidoNombre(
        @RequestParam String apellido,
        @RequestParam String nombre
    ) {
        return ResponseEntity.ok(
            reservaService.buscarPorApellidoNombre(apellido, nombre)
        );
    }

    @Operation(
        summary = "Cancelar reserva",
        description = "Cancela una reserva utilizando el patrón State (cambia el estado a cancelada)"
    )
    @PutMapping("/cancelar")
    public ResponseEntity<?> cancelarReserva(@RequestBody ReservaDTO dto) {
        return ResponseEntity.ok(
            reservaService.cancelarReserva(dto.getId_reserva())
        );
    }
}
