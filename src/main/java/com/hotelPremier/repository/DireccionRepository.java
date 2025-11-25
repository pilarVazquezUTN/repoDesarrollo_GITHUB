package com.hotelPremier.repository;

import com.hotelPremier.classes.direccion.Direccion;
import com.hotelPremier.classes.huesped.Huesped;
import com.hotelPremier.classes.huesped.HuespedID;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion,Integer> {

    @Override
    Direccion save(Direccion direccion);
}
