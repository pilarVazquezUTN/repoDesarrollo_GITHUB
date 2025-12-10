package com.hotelPremier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hotelPremier.classes.Dominio.Estadia;
import java.util.List;

@Repository
public interface EstadiaRepository extends JpaRepository<Estadia,Integer> {
    @Query("""
    SELECT e
    FROM Estadia e
    LEFT JOIN e.listafactura f
    WHERE f IS NULL
    """)
    List<Estadia> estadiasSinFactura();

        
}
