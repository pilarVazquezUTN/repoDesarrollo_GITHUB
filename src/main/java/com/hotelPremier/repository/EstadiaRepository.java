package com.hotelPremier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hotelPremier.classes.Dominio.Estadia;


@Repository
public interface EstadiaRepository extends JpaRepository<Estadia,Integer> {



    
}
