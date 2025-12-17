package com.hotelPremier.repository;

import com.hotelPremier.classes.Dominio.Estadia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstadiaRepository extends JpaRepository<Estadia, Integer> {

    @Query("""
        SELECT e
        FROM Estadia e
        WHERE e.listafactura IS EMPTY
    """)
    List<Estadia> estadiasSinFactura();

    @Query("""
        SELECT e 
        FROM Estadia e
        WHERE e.habitacion.numero = :numero
        AND e.estado = 'ENCURSO'
    """)
    Estadia estadiaEnCurso(Integer numero);

}
