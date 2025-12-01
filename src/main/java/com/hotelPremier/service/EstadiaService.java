
package com.hotelPremier.service;

import java.util.ArrayList;
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

    public void guardarListaEstadia(List<EstadiaDTO> listaestadiaDTO){
        List<Estadia> listaestadia = new ArrayList<>();
        listaestadia = mapper.toEntityEstadia(listaestadiaDTO);

        //por cada estadia, la voy cargando en la bdd.
        //tambien actualizo cada reserva que haya superpuesto.
        for(Estadia e : listaestadia){
            
            estadiaRepository.save(e);//me guardo una estadia en la bdd
        }
    }
    //@Autowired
    //private ClassMapper


    /*public List<EstadiaDTO> getListaEstadias(){
        
        return 
    }*/
}



