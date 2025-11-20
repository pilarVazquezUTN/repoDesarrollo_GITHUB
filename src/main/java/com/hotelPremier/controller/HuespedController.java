package com.hotelPremier.controller;
import java.util.List;
import java.util.Set;


import com.hotelPremier.service.HuespedServiceImpl;

import org.apache.catalina.connector.Response;
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
    private HuespedServiceImpl huespedService;

    /*
        los end path van en plural.
        no se pone dar baja/alta, se pone solo el huespedes y se va cambiando el get/post/ etc.
    */
    @GetMapping("/huespedes")
    public ResponseEntity<List<HuespedDTO>> getHuesped(
        @RequestParam(value="DNI") String DNI) { 
        List<HuespedDTO> listaHuespedes=null;
        if(DNI.equals("listaHuespedes")){
            listaHuespedes=huespedService.findAll();
        } else{
            listaHuespedes=huespedService.findByCategory(DNI);
        }

        return new ResponseEntity<>(listaHuespedes, HttpStatus.OK);
    }
    
    @PostMapping("/huespedes")
    public ResponseEntity<HuespedDTO> addHuesped(@RequestBody HuespedDTO huesped) {
        HuespedDTO addedHuesped = huespedService.addHuesped(huesped);
        return new ResponseEntity<>(addedHuesped, HttpStatus.OK);

    }
    @DeleteMapping("/huespedes")
    public ResponseEntity<HuespedDTO> deleteHuesped(@PathVariable long DNI) {
        huespedService.deleteHuesped(DNI);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT); 
        /*
        se puede devolver el usuario que eliminamos. */

    }

}
