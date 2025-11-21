package com.hotelPremier.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hotelPremier.classes.huesped.Huesped;

@Repository
public interface HuespedRepositoryDAO extends CrudRepository<Huesped,Long>{

    @Override
    List<Huesped> findAll();
    List<Huesped> findByHuespedID_Dni(String dni);
    
}
