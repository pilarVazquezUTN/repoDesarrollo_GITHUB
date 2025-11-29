package com.hotelPremier.repository;

import com.hotelPremier.classes.habitacion.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion,Integer> {

    List<Habitacion> findAll();
   // List<Habitacion> findByTipo(String tipo);
    
   @Query("""
        SELECT h 
        FROM Habitacion h 
        WHERE h.tipohabitacion = :tipoHabitacion             
    """) 
   List<Habitacion> buscarPorTipoHabitacion(@Param("tipoHabitacion")    String tipoHabitacion);


   @Query("""
        SELECT h, r, e
        FROM Habitacion h
            LEFT JOIN Reserva r 
                WITH r.fecha_desde <= :fechaHasta 
                AND r.fecha_hasta >= :fechaDesde
            LEFT JOIN Estadia e 
                WITH e.checkin <= :fechaHasta 
                AND e.checkout >= :fechaDesde
        WHERE h.tipohabitacion = :tipoHabitacion
    """)
    List<Habitacion> buscarListaHabitaciones(
        @Param("tipoHabitacion") String tipoHabitacion,
        @Param("fechaDesde") Date fechaDesde,
        @Param("fechaHasta") Date fechaHasta
    );

}
