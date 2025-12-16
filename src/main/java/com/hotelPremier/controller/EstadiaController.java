package com.hotelPremier.controller;

import com.hotelPremier.service.EstadiaService;
import com.hotelPremier.classes.DTO.EstadiaDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> obtenerEnCurso(
        @Parameter(description = "Número de habitación", example = "101")
        @PathVariable Integer numHabitacion
    ) {

        EstadiaDTO dto = estadiaService.obtenerEstadiaEnCurso(numHabitacion);

        if (dto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No hay estadía ENCURSO para la habitación " + numHabitacion);
        }

        return ResponseEntity.ok(dto);
    }
}
