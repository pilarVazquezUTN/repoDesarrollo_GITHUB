package com.hotelPremier.controller;
import java.util.List;
import java.util.Set;

import com.hotelPremier.service.HuespedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hotelPremier.classes.huesped.Huesped;
import com.hotelPremier.classes.huesped.HuespedDTO;

@RestController
public class HuespedController {
    
    @Autowired 
    private HuespedService huespedService;

    @GetMapping("/buscarHuesped")
    public ResponseEntity<List<HuespedDTO>> getHuesped(
        @RequestParam(value="DNI") String DNI)
        List<Huesped> listaHuespedes=null;
        if(DNI.equals("listaHuespedes")){
            listaHuespedes=huespedService.findAll();
        } else{
            listaHuespedes=huespedService.findByCategory(DNI);
        }
    )
    
    @PostMapping("/darAltaHuesped")
    public ResponseEntity<Huesped> addHuesped(@RequestBody Huesped huesped) {
        Huesped addedHuesped = huespedService.addHuesped(huesped);
        return new ResponseEntity<>(addedHuesped, HttpStatus.OK);

    }
    @DeleteMapping("/darBajaHuesped")
    public ResponseEntity<HuespedDTO> deleteHuesped(@PathVariable long DNI) {
        huespedService.deleteHuesped(DNI);
        return new ResponseEntity<>(Response.noErrorResponse(), HttpStatus.OK);

    }

}
