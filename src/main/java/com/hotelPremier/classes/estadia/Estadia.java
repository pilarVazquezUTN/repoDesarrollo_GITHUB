package com.hotelPremier.classes.estadia;

import java.util.Date;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+

@Entity
@Table(name="estadia")
public class Estadia {
    @Id
    private Integer id_estadia;
    private Date checkin;
    private Date checkout;
    @ManyToOne //de estadia a habitacion
    private Integer nro_habitacion;
}