package com.hotelPremier.repository;

import com.hotelPremier.classes.estadia.Estadia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface EstadiaRepository extends JpaRepository<Estadia,Integer> {



    
}
