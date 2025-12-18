package com.hotelPremier.controller;

import com.hotelPremier.service.EstadiaService;
import com.hotelPremier.classes.DTO.EstadiaDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/estadias")
@Tag(name = "Estadías", description = "Operaciones relacionadas con las estadías del hotel")
public class EstadiaController {

    @Autowired
    private EstadiaService estadiaService;

    @Operation(
        summary = "Obtener estadía en curso por número de habitación",
        description = "Devuelve la estadía que se encuentra en estado ENCURSO para una habitación determinada"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estadía encontrada"),
        @ApiResponse(responseCode = "404", description = "No existe estadía en curso para la habitación")
    })
    @GetMapping("/enCurso/{numHabitacion}")
    public ResponseEntity<EstadiaDTO> obtenerEnCurso(
        @Parameter(description = "Número de habitación", example = "101")
        @PathVariable Integer numHabitacion
    ) {
        EstadiaDTO dto = estadiaService.obtenerEstadiaEnCurso(numHabitacion);
        return ResponseEntity.ok(dto);
    }

    @Operation(
        summary = "Agregar nueva estadía",
        description = "Crea una nueva estadía para una habitación con los huéspedes especificados. " +
                      "La habitación no debe tener otra estadía en curso y los huéspedes deben existir en el sistema."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estadía creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o habitación ya ocupada"),
        @ApiResponse(responseCode = "404", description = "Habitación o huésped no encontrado")
    })
    @PostMapping
    public ResponseEntity<EstadiaDTO> agregarEstadia(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de la estadía a crear",
            required = true
        )
        @RequestBody EstadiaDTO dto
    ) {
        EstadiaDTO estadiaCreada = estadiaService.agregarEstadia(dto);
        return ResponseEntity.ok(estadiaCreada);
    }
}
