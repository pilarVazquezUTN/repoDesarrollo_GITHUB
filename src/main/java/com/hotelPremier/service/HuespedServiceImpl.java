package com.hotelPremier.service;//package com.hotelPremier.service;

import com.hotelPremier.repository.HuespedRepositoryDAO;
import org.springframework.stereotype.Service;

import com.hotelPremier.classes.huesped.Huesped;
import com.hotelPremier.classes.huesped.HuespedDTO;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;


@Service
public class HuespedServiceImpl implements HuespedService {

    @Autowired
    private HuespedRepositoryDAO huespedRepository;

    @Override
    public List<Huesped> findAll(){

    }

    @Override
    public List<Huesped> findByCategory(String DNI) {
        //return List.of();
    }

    @Override
    public Huesped addHuesped(Huesped huesped) {
        return null;
    }

    @Override
    public void deleteHuesped(long DNI) {

    }

    @Override
    public HuespedDTO addHuesped(HuespedDTO huespedDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addHuesped'");
    }

}
