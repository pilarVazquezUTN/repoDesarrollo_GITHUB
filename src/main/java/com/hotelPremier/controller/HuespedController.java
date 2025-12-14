package com.hotelPremier.controller;
import java.util.List;


import com.hotelPremier.service.HuespedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotelPremier.classes.DTO.HuespedDTO;

@RestController

public class HuespedController {
    
    @Autowired 
    private HuespedService huespedService;

    /*
        los end path van en plural.
        no se pone dar baja/alta, se pone solo el huespedes y se va cambiando el get/post/ etc.
    */
    @GetMapping("/huespedes")
    public ResponseEntity<List<HuespedDTO>> getHuespedes(
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String tipoDocumento
    ) {
        dni = (dni != null && dni.isEmpty()) ? null : dni;
        nombre = (nombre != null && nombre.isEmpty()) ? null : nombre;
        apellido = (apellido != null && apellido.isEmpty()) ? null : apellido;
        tipoDocumento = (tipoDocumento != null && tipoDocumento.isEmpty()) ? null : tipoDocumento;

        return ResponseEntity.ok(
            huespedService.buscarHuespedes(dni, nombre, apellido, tipoDocumento)
        );
    }
    
    @PostMapping("/huespedes")
    public ResponseEntity<HuespedDTO> addHuesped(@RequestBody HuespedDTO huesped) {
        HuespedDTO addedHuesped = huespedService.addHuesped(huesped);
        return new ResponseEntity<>(addedHuesped, HttpStatus.OK);
    }
    
    @DeleteMapping("/huespedes/{tipo}/{dni}")
    public ResponseEntity<?> deleteHuesped(
            @PathVariable String tipo,
            @PathVariable String dni
    ) {
        huespedService.deleteHuesped(tipo, dni);
        return ResponseEntity.noContent().build();
    }


}
