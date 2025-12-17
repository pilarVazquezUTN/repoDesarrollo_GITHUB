package com.hotelPremier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotelPremier.classes.Dominio.NotaDeCredito;


@Repository
public interface NotaDeCreditoRepository extends JpaRepository<NotaDeCredito,Integer> {


    
}
