package com.hotelPremier.controller;

import com.hotelPremier.service.ReservaServiceImpl;
import org.springframework.web.bind.annotation.*;

import com.hotelPremier.classes.reserva.GestorReservaService;
import com.hotelPremier.classes.reserva.ReservaDTO;

import java.util.List;


@RestController
// 2. Define la URL base de tu API
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "http://localhost:8080")
public class ReservaController {

    private final ReservaServiceImpl reservaService;



    public ReservaController(ReservaServiceImpl reservaService) {
        this.reservaService = reservaService;
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

        //esto me devuelve del service un DTO
        return reservaService.findByApellidoContainingIgnoreCase(apellido);

    }
}

