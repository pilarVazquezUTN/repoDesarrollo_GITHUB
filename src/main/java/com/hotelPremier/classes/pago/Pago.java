package com.hotelPremier.classes.pago;

import java.util.Date;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+

@Entity
@Table(name="pago")
public class Pago {
    private float monto;
    private String medioPago;
    private Date  fecha;

}
