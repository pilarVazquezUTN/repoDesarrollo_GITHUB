//package com.hotelPremier.controller;

import org.springframework.web.bind.annotation.*;

import Classes.Reserva.GestorReservaService;
import Classes.Reserva.ReservaDTO;

import java.util.List;


@RestController
// 2. Define la URL base de tu API
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "http://localhost:3000")
public class ReservaRestController {

    private final GestorReservaService gestorReservaService;

    // 4. Inyección de Dependencias: Spring provee la instancia del servicio
    public ReservaRestController(GestorReservaService gestorReservaService) {
        this.gestorReservaService = gestorReservaService;
    }

    /**
     * Endpoint para obtener la lista de reservas.
     * Mapea a: GET http://localhost:8080/api/reservas?apellido=Perez
     */
    @GetMapping
    public List<ReservaDTO> getReservas(
            // @RequestParam mapea el parámetro 'apellido' de la URL
            @RequestParam(required = false) String apellido) {

        // 5. Llama al servicio, que maneja la lógica de búsqueda y el mapeo a DTOs
        return gestorReservaService.reservas(apellido);
    }
}

