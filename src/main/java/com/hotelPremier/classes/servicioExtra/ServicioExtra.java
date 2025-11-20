package com.hotelPremier.classes.servicioExtra;

import com.hotelPremier.classes.estadia.Estadia;
import jakarta.persistence.*;

@Entity
@Table(name="servicio_extra")
public class ServicioExtra {


    @EmbeddedId
    private ServicioExtraID servicioExtraID;


    @ManyToOne
    @JoinColumn(name = "id_estadia",insertable = false, updatable = false)
    private Estadia estadia; // La Entidad completa

    @Column(name="tipo_servicio")
    private String tipo_servicio ;
    @Column(name="precio")
    private Float precio ;



}
