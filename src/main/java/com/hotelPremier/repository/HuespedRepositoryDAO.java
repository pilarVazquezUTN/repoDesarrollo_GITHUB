package com.hotelPremier.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hotelPremier.classes.direccion.Direccion;
import com.hotelPremier.classes.huesped.Huesped;
import com.hotelPremier.classes.huesped.HuespedDTO;
import com.hotelPremier.classes.huesped.HuespedID;

@Repository
public interface HuespedRepositoryDAO extends JpaRepository<Huesped,HuespedID>{

    @Override
    List<Huesped> findAll();
    List<Huesped> findByHuespedID_Dni(String dni);
    //HuespedDTO addHuesped(HuespedDTO huespedDTO);  
    Huesped save(Huesped huesped);
    //void save(Direccion direccion); ------> VA EN REPOSITORY DIRECCION.

    @Query("""
    SELECT h 
    FROM Huesped h
    WHERE (:dni IS NULL OR h.huespedID.dni = :dni)
        AND   (:nombre IS NULL OR h.nombre ILIKE :nombre)
        AND   (:apellido IS NULL OR h.apellido ILIKE :apellido)
        AND   (:tipoDocumento IS NULL OR h.huespedID.tipoDocumento = :tipoDocumento)
    """)
    // OR LOWER(h.nombre)
    // OR LOWER(h.apellido)
    //
    List<Huesped> buscarHuespedes(
        @Param("dni") String dni, 
        @Param("nombre") String nombre, 
        @Param("apellido") String apellido, 
        @Param("tipoDocumento")String tipoDocumento 
    );
}
