package com.hotelPremier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hotelPremier.classes.Dominio.responsablePago.*;

import java.util.Optional;

@Repository
public interface ResponsablePagoRepository extends JpaRepository<ResponsablePago, Integer> {
    
    /**
     * Busca un ResponsablePago de tipo PersonaFisica por DNI y tipo de documento.
     */
    @Query("""
        SELECT pf 
        FROM PersonaFisica pf 
        WHERE pf.huesped.huespedID.dni = :dni 
        AND pf.huesped.huespedID.tipoDocumento = :tipoDocumento
    """)
    Optional<PersonaFisica> findPersonaFisicaByDniAndTipoDocumento(
        @Param("dni") String dni, 
        @Param("tipoDocumento") String tipoDocumento
    );
    
    /**
     * Busca un ResponsablePago de tipo PersonaJuridica por CUIT.
     */
    @Query("SELECT pj FROM PersonaJuridica pj WHERE pj.cuit = :cuit")
    Optional<PersonaJuridica> findPersonaJuridicaByCuit(@Param("cuit") String cuit);
}
