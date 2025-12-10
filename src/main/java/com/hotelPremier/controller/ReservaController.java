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
    // 4) ELIMINAR RESERVA POR ID
    // ==========================================================
    @DeleteMapping
    public ResponseEntity<?> eliminarReserva(@RequestBody ReservaDTO dto) {

        if (dto.getId_reserva() == null) {
            return ResponseEntity.badRequest()
                    .body("Debe enviar un id_reserva dentro del JSON");
        }

        boolean eliminada = reservaService.deleteReserva(dto.getId_reserva());

        if (!eliminada) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No existe una reserva con ID " + dto.getId_reserva());
        }

        return ResponseEntity.ok("Reserva eliminada correctamente");
    }

}
