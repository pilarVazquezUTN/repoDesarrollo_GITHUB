package com.hotelPremier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

import com.hotelPremier.classes.reserva.Reserva;


@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {


    List<Reserva> findByApellidoContainingIgnoreCase(String apellido);
}
