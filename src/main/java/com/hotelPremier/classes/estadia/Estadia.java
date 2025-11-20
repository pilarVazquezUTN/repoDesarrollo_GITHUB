package com.hotelPremier.classes.estadia;

import java.util.Date;

import com.hotelPremier.classes.habitacion.Habitacion;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+

@Entity
@Table(name="estadia")
public class Estadia { 
    @Id
    @Column(name="id_estadia")
    private Integer id_estadia;
    @Column(name="checkin")
    private Date checkin;
    @Column(name="checkout")
    private Date checkout;

    @ManyToOne //de estadia a habitacion
    @JoinColumn(name = "nro_habitacion")
    private Habitacion habitacion;


    public Integer getId_estadia() {
        return id_estadia;
    }
    public void setId_estadia(Integer id_estadia) {
        this.id_estadia = id_estadia;
    }

}