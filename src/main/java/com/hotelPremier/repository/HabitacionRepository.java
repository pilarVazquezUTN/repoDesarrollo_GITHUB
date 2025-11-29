package com.hotelPremier.repository;

import com.hotelPremier.classes.habitacion.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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







}
