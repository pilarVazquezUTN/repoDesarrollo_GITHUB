package com.hotelPremier.controller;

import com.hotelPremier.classes.DTO.HabitacionDTO;
// import com.hotelPremier.classes.Dominio.Huesped;
import com.hotelPremier.service.HabitacionService;

// import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;


@RestController
@Tag(name = "Habitaciones", description = "Consultas y disponibilidad de habitaciones")
public class HabitacionController {

    @Autowired
    HabitacionService habitacionService;

    @Operation(summary = "Listar habitaciones por tipo")
    @GetMapping("/habitaciones")
    public ResponseEntity<List<HabitacionDTO>> getHabitaciones(
        @Parameter(description = "Tipo de habitación", example = "DOBLE")
        @RequestParam String tipo
    ){
        return ResponseEntity.ok(habitacionService.getHabitaciones(tipo));
    }

    // @Operation(summary = "Obtener habitación por número")
    // @GetMapping("/numHabitacion")
    // public HabitacionDTO getHabitacion(
    //     @Parameter(description = "Número de habitación", example = "101")
    //     @RequestParam Integer numero
    // ){
    //     return habitacionService.getHabitacion(numero);
    // }

    // @Operation(summary = "Buscar habitaciones disponibles entre fechas")
    // @GetMapping("/listaHabitaciones") 
    // public ResponseEntity<List<HabitacionDTO>> buscarListaHabitaciones(
    //     @RequestParam String tipo,
    //     @RequestParam Date fechaDesde,
    //     @RequestParam Date fechaHasta
    // ){
    //     return ResponseEntity.ok(
    //         habitacionService.buscarListaHabitaciones(tipo, fechaDesde, fechaHasta)
    //     );
    // }

    // @Operation(summary = "Obtener habitaciones con detalle de estado")
    // @GetMapping("/detalleHabitaciones")
    // public ResponseEntity<List<HabitacionDTO>> obtenerHabitacionesConDetalle(
    //     @RequestParam String tipo,
    //     @RequestParam Date fechaDesde,
    //     @RequestParam Date fechaHasta
    // ){
    //     return ResponseEntity.ok(
    //         habitacionService.obtenerHabitacionesConDetalle(tipo, fechaDesde, fechaHasta)
    //     );
    // }

    // @Operation(summary = "Listar huéspedes de una habitación")
    // @GetMapping("/{nro}/huespedes")
    // public List<Huesped> getHuespedesDeHabitacion(
    //     @Parameter(description = "Número de habitación", example = "101")
    //     @PathVariable Integer nro
    // ) {
    //     return habitacionService.obtenerHuespedesPorHabitacion(nro);
    // }
}
