package com.hotelPremier.repository;

import com.hotelPremier.classes.Dominio.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    // =============================
    // BUSCAR ENTRE FECHAS
    // =============================
    @Query("""
        SELECT r FROM Reserva r
        WHERE r.fecha_desde <= :hasta
          AND r.fecha_hasta >= :desde
    """)
    List<Reserva> buscarEntreFechas(
            @Param("desde") Date desde,
            @Param("hasta") Date hasta
    );

    // =============================
    // BUSCAR POR APELLIDO / NOMBRE
    // =============================
    List<Reserva> findByApellidoStartingWithIgnoreCase(String apellido);

    List<Reserva> findByNombreStartingWithIgnoreCase(String nombre);

    List<Reserva> findByApellidoStartingWithIgnoreCaseAndNombreStartingWithIgnoreCase(
            String apellido,
            String nombre
    );

    // =============================
    // VALIDAR SOLAPAMIENTO
    // =============================
    @Query("""
        SELECT COUNT(r) FROM Reserva r
        WHERE r.habitacion.numero = :numero
          AND r.fecha_desde <= :hasta
          AND r.fecha_hasta >= :desde
    """)
    Integer haySuperposicion(
            @Param("numero") Integer numero,
            @Param("desde") Date desde,
            @Param("hasta") Date hasta
    );
}
