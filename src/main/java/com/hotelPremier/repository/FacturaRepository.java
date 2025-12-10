package com.hotelPremier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hotelPremier.classes.Dominio.Factura;
import java.util.List;



@Repository
public interface FacturaRepository extends JpaRepository<Factura,Integer> {
        @Query("""
        SELECT f
        FROM Factura f
        WHERE f.responsablepago.huesped.huespedID.dni = :dni
    """)
    List<Factura> buscarPorDni(@Param("dni") String dni);



    
}
