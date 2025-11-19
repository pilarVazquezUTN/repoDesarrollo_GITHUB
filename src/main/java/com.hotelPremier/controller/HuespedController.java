//package com.hotelPremier.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Classes.Huesped.HuespedDTO;

@RestController
public class HuespedController {
    
    @Autowired 
    private HuespedService huespedService;

    @GetMapping("/buscarHuesped")
    public ResponseEntity<List<HuespedDTO>> getHuesped(RequestParam(value="DNI") String DNI)
        List<Huesped> listaHuespedes=null;
        if(category.equals("listaHuespedes")){
            listaHuespedes=huespedService.findAll();
        } else{
            listaHuespedes=huespedService.findByCategory(DNI);
        }
    )
    @GetMapping("/darAltaHuesped")
    @GetMapping("/darBajaHuesped")

}
