package com.hotelPremier.repository;

import com.hotelPremier.classes.Dominio.servicioExtra.ServicioExtra;
import com.hotelPremier.classes.Dominio.servicioExtra.ServicioExtraID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicioExtraRepository extends JpaRepository<ServicioExtra, ServicioExtraID> {

    @Query("SELECT s FROM ServicioExtra s WHERE s.servicioExtraID.id_estadia = :idEstadia")
    List<ServicioExtra> findByEstadiaId(@Param("idEstadia") Integer idEstadia);

    @Query("SELECT s FROM ServicioExtra s WHERE s.servicioExtraID.id_estadia = :idEstadia AND s.servicioExtraID.id_servicio IN :ids")
    List<ServicioExtra> findByEstadiaIdAndServicioIds(@Param("idEstadia") Integer idEstadia, @Param("ids") List<Integer> ids);
}

