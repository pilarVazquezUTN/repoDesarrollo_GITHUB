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
    public ResponseEntity<List<ReservaDTO>> guardarReservas(@RequestBody List<ReservaDTO> listaDTO) {
        List<ReservaDTO> reservasGuardadas = reservaService.guardarLista(listaDTO);
        return ResponseEntity.ok(reservasGuardadas);
    }

    @Operation(summary = "Buscar reservas entre fechas")
    @GetMapping
    public ResponseEntity<List<ReservaDTO>> buscarEntreFechas(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta
    ) {
        List<ReservaDTO> reservas = reservaService.buscarEntreFechas(desde, hasta);
        return ResponseEntity.ok(reservas);
    }


    @Operation(summary = "Buscar reservas por apellido y nombre")
    @GetMapping("/buscar")
    public ResponseEntity<List<ReservaDTO>> buscarPorApellidoNombre(
        @RequestParam String apellido,
        @RequestParam String nombre
    ) {
        List<ReservaDTO> reservas = reservaService.buscarPorApellidoNombre(apellido, nombre);
        return ResponseEntity.ok(reservas);
    }

    @Operation(
        summary = "Cancelar reserva",
        description = "Cancela una reserva utilizando el patrón State (cambia el estado a cancelada). Solo permite cancelar reservas en estado PENDIENTE."
    )
    @PutMapping("/cancelar")
    public ResponseEntity<ReservaDTO> cancelarReserva(@RequestBody ReservaDTO dto) {
        ReservaDTO reservaCancelada = reservaService.cancelarReserva(dto.getId_reserva());
        return ResponseEntity.ok(reservaCancelada);
    }
}
