package com.hotelPremier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;
import org.springframework.stereotype.Repository;

import com.hotelPremier.classes.reserva.Reserva;


@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {


    List<Reserva> findByApellidoContainingIgnoreCase(String apellido);

    @Query("""
        SELECT r 
        FROM Reserva r 
        WHERE  r.fecha_desde <= :fechahasta
            AND r.fecha_hasta >= :fechadesde
    """)
    List<Reserva>  buscarReservas(@Param("fechadesde") Date fechaDesde, @Param("fechahasta") Date fechaHasta);




}
