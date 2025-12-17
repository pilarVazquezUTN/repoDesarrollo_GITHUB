package com.hotelPremier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotelPremier.classes.Dominio.Direccion;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion,Integer> {

    @Override
    Direccion save(Direccion direccion);
}
