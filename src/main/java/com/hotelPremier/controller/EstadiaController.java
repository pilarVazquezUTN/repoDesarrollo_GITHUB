package com.hotelPremier.controller;

import com.hotelPremier.classes.DTO.EstadiaDTO;
import com.hotelPremier.repository.EstadiaRepository;
import com.hotelPremier.service.EstadiaService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class EstadiaController {

    @Autowired
    private EstadiaService estadiaService; 

    @PostMapping("/estadias")
    public ResponseEntity<String> guardarEstadia(@RequestBody EstadiaDTO estadiaDTO) {
        estadiaService.guardarEstadia(estadiaDTO);
        return ResponseEntity.ok("Estadía guardada correctamente");
    }

    @PostMapping("/listaEstadias")
    public ResponseEntity<String> guardarListaEstadias(
        @RequestBody List<EstadiaDTO> listaestadiaDTO
    ){
        estadiaService.guardarListaEstadia(listaestadiaDTO);
        return ResponseEntity.ok("Estadía guardada correctamente");
    }   

    @GetMapping("/sin-factura")
    public ResponseEntity<List<EstadiaDTO>> getEstadiasSinFactura() {
        List<EstadiaDTO> lista = estadiaService.obtenerEstadiasSinFactura();
        return ResponseEntity.ok(lista);
    }


}
