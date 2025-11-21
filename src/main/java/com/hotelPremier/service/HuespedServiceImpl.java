package com.hotelPremier.service;//package com.hotelPremier.service;

import com.hotelPremier.repository.HuespedRepositoryDAO;
import org.springframework.stereotype.Service;

import com.hotelPremier.classes.huesped.Huesped;
import com.hotelPremier.classes.huesped.HuespedDTO;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.hotelPremier.classes.FuncionesUtiles;



@Service
public class HuespedServiceImpl{

    FuncionesUtiles funcionesUtiles = new FuncionesUtiles();

    @Autowired
    private HuespedRepositoryDAO huespedRepository;

    public List<HuespedDTO> findAll(){
        /* huespedRepository.findAll() nos devuelve un Huesped. Hay que pasarlo a HuespedDTO */
        return funcionesUtiles.mapToDTOList(huespedRepository.findAll()); 
    }

    
    public List<HuespedDTO> findByDni(String dni) {
        
        return null;
    }

    
    public void deleteHuesped(String dni) {

    }

    
    public HuespedDTO addHuesped(HuespedDTO huespedDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addHuesped'");
    }

    public List<HuespedDTO> findByCategory(String dni) {
        return null;
    }
}
