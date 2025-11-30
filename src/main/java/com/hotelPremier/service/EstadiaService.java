
package com.hotelPremier.service;

import java.util.List;

import com.hotelPremier.classes.estadia.Estadia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotelPremier.repository.EstadiaRepository;
import com.hotelPremier.classes.estadia.EstadiaDTO;
import com.hotelPremier.classes.mapper.ClassMapper;

@Service
public class EstadiaService {

    @Autowired
    private EstadiaRepository estadiaRepository;

    @Autowired
    private ClassMapper mapper;

    public void guardarEstadia(EstadiaDTO estadiaDTO){
        Estadia estadia = new Estadia();
        estadia= mapper.toEntity(estadiaDTO);
        estadiaRepository.save(estadia);

    }
    //@Autowired
    //private ClassMapper


    /*public List<EstadiaDTO> getListaEstadias(){
        
        return 
    }*/
}



