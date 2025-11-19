package com.hotelPremier.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import classes.Huesped.Huesped;

@Repository
public interface HuespedRepository extends CrudRepository<Huesped,Long>{

    @Override
    List<Huesped> findAll();
    List<Huesped> findByCategory(String DNI);
    
}
