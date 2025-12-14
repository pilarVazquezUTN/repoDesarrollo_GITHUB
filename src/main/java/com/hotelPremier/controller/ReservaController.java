package com.hotelPremier.controller;

import com.hotelPremier.classes.DTO.ReservaDTO;
import com.hotelPremier.service.ReservaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    // ==========================================================
    // 1) GUARDAR RESERVA
    // ==========================================================
    @PostMapping
    public ResponseEntity<?> guardarReservas(@RequestBody List<ReservaDTO> listaDTO) {
        List<ReservaDTO> guardadas = reservaService.guardarLista(listaDTO);
        return ResponseEntity.ok(guardadas);
    }

    // ==========================================================
    // 2) BUSCAR ENTRE FECHAS
    // ==========================================================
    @GetMapping
    public ResponseEntity<?> buscarEntreFechas(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date desde,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date hasta
    ) {

        if (desde == null || hasta == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Debe enviar parámetros ?desde=YYYY-MM-DD&hasta=YYYY-MM-DD");
        }

        return ResponseEntity.ok(reservaService.buscarEntreFechas(desde, hasta));
    }

    // ==========================================================
    // 3) BUSCAR POR APELLIDO + NOMBRE (dinámico)
    // ==========================================================
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPorApellidoNombre(
            @RequestParam(required = false, defaultValue = "") String apellido,
            @RequestParam(required = false, defaultValue = "") String nombre
    ) {
        return ResponseEntity.ok(reservaService.buscarPorApellidoNombre(apellido, nombre));
    }

    // ==========================================================
    // 4) CANCELAR RESERVA POR ID (CU06)
    // ==========================================================
    /**
     * Cancela una reserva cambiando su estado a CANCELADA usando el patrón State.
     * No se elimina físicamente la reserva, solo se cambia su estado.
     * 
     * @param dto DTO con el id_reserva a cancelar
     * @return Respuesta con el resultado de la operación
     */
    @PutMapping("/cancelar")
    public ResponseEntity<?> cancelarReserva(@RequestBody ReservaDTO dto) {

        if (dto.getId_reserva() == null) {
            return ResponseEntity.badRequest()
                    .body("Debe enviar un id_reserva dentro del JSON");
        }

        try {
            ReservaDTO reservaCancelada = reservaService.cancelarReserva(dto.getId_reserva());
            return ResponseEntity.ok(reservaCancelada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

}
