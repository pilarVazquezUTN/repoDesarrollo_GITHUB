package com.hotelPremier.classes.medioDePago;

import java.util.Date;

import com.hotelPremier.classes.pago.Pago;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+

@Entity
@Table(name="medio_pago")
public class MedioDePago {
    @Id 
    private Integer id_mediodepago;
    private int monto;
    private Date fecha;
    @ManyToOne
    @JoinColumn(name = "id_pago")
    private Pago pago;
}
