package com.hotelPremier.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hotelPremier.classes.DTO.HuespedDTO;
import com.hotelPremier.classes.Dominio.Direccion;
import com.hotelPremier.classes.Dominio.Huesped;
import com.hotelPremier.classes.Dominio.HuespedID;

@Repository
public interface HuespedRepository extends JpaRepository<Huesped,HuespedID>{

    @Override
    List<Huesped> findAll();
    List<Huesped> findByHuespedID_Dni(String dni);
    //HuespedDTO addHuesped(HuespedDTO huespedDTO);  
    Huesped save(Huesped huesped);
    //void save(Direccion direccion); ------> VA EN REPOSITORY DIRECCION.

    /* QUERY PARA BUSQUEDA POR: EMPIEZA CON
    
            @Query("""
            SELECT h
            FROM Huesped h
            WHERE (:dni IS NULL OR h.huespedID.dni = :dni)
                AND (:nombre IS NULL OR CAST(h.nombre AS string) ILIKE CONCAT(:nombre, '%'))
                AND (:apellido IS NULL OR CAST(h.apellido AS string) ILIKE CONCAT(:apellido, '%'))
                AND (:tipoDocumento IS NULL OR h.huespedID.tipoDocumento = :tipoDocumento)
            """)
    */
    @Query("""
    SELECT h 
    FROM Huesped h
    WHERE 
        (:dni IS NULL OR :dni = '' OR h.huespedID.dni ILIKE CONCAT(:dni, '%'))
    AND (:nombre IS NULL OR :nombre = '' OR h.nombre ILIKE CONCAT(:nombre, '%'))
    AND (:apellido IS NULL OR :apellido = '' OR h.apellido ILIKE CONCAT(:apellido, '%'))
    AND (:tipoDocumento IS NULL OR :tipoDocumento = '' OR h.huespedID.tipoDocumento = :tipoDocumento)
""")
List<Huesped> buscarHuespedes(
    @Param("dni") String dni, 
    @Param("nombre") String nombre, 
    @Param("apellido") String apellido, 
    @Param("tipoDocumento") String tipoDocumento
);

}
