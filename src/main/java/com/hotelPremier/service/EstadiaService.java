
package com.hotelPremier.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotelPremier.repository.EstadiaRepository;
import com.hotelPremier.classes.estadia.EstadiaDTO;
import com.hotelPremier.classes.mapper.ClassMapper;

@Service
public class EstadiaService {

    @Autowired
    private EstadiaRepository estadiaRepository;

    //@Autowired
    //private ClassMapper


    /*public List<EstadiaDTO> getListaEstadias(){
        
        return 
    }*/
}



