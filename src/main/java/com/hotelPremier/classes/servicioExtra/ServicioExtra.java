package com.hotelPremier.classes.servicioExtra;

import com.hotelPremier.classes.estadia.Estadia;
import jakarta.persistence.*;

@Entity
@Table(name="servicio_extra")
public class ServicioExtra {


    @EmbeddedId
    private ServicioExtraID servicioExtraID;


    @ManyToOne
    @JoinColumn(name = "id_estadia")
    private Estadia estadia; // La Entidad completa


    private String tipo_servicio ;
    private Float precio ;



}
