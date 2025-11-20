package com.hotelPremier.classes.pago;

import java.util.Date;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+

@Entity
@Table(name="pago")
public class Pago {
    @Id
    @Column(name="id_pago")
    private Integer id_pago;
    @Column(name="monto")
    private float monto;
    @Column(name="medioPago")
    private String medioPago;
    @Column(name="fecha")
    private Date  fecha;

}
