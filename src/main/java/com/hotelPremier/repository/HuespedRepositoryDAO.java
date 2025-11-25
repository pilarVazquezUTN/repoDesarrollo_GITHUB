package com.hotelPremier.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
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
    void save(Direccion direccion);

    @Query("""
    SELECT h FROM Huesped h
    WHERE (:dni IS NULL OR h.huespedID.dni = :dni)
    AND   (:nombre IS NULL OR LOWER(h.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
    AND   (:apellido IS NULL OR LOWER(h.apellido) LIKE LOWER(CONCAT('%', :apellido, '%')))
    AND   (:tipoDocumento IS NULL OR h.huespedID.tipoDocumento = :tipoDocumento)
""")
List<Huesped> buscarHuespedes(String dni, String nombre, String apellido, String tipoDocumento);
}
