package com.hotelPremier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;

import com.hotelPremier.classes.Dominio.Reserva;


@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {


    List<Reserva> findByApellidoContainingIgnoreCase(String apellido);

    @Query("""
        SELECT r 
        FROM Reserva r 
        WHERE  r.fecha_desde <= :fechahasta
            AND r.fecha_hasta >= :fechadesde
    """)
    List<Reserva>  buscarReservas(@Param("fechadesde") Date fechaDesde, @Param("fechahasta") Date fechaHasta);


    @Query("""
    SELECT r.id_reserva
    FROM Reserva r
     WHERE r.fecha_desde <= :fechahasta
      AND r.fecha_hasta >= :fechadesde
      AND r.nro_habitacion.numero = :nro_hab
""")
    Integer encontrarIdReserva(@Param("fechadesde") Date fechaDesde, @Param("fechahasta") Date fechaHasta,@Param("nro_hab") Integer nroHab);
   // void delete(Reserva r);

    @Query("""
    SELECT r
    FROM Reserva r
    WHERE (:apellido IS NULL OR LOWER(r.apellido) LIKE LOWER(CONCAT(:apellido, '%')))
      AND (:nombre IS NULL OR LOWER(r.nombre) LIKE LOWER(CONCAT(:nombre, '%')))
    """)
    List<Reserva> buscarPorApellidoNombre(
            @Param("apellido") String apellido,
            @Param("nombre") String nombre
    );

}
