package com.hotelPremier.classes.medioDePago;

import java.util.Date;

import com.hotelPremier.classes.pago.Pago;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+

@Entity
@Table(name="medio_pago")
public class MedioDePago {
    @Id 
    @Column(name="id_mediodepago")
    private Integer id_mediodepago;
    @Column(name="monto")
    private int monto;
    @Column(name="fecha")
    private Date fecha;

    @ManyToOne
    @JoinColumn(name = "id_pago")
    private Pago pago;

    @Column(name="tipomediodepago")
    private String tipomediodepago;

    @Column(name="plazo")
    private Date plazo;

    @Column(name="banco")
    private String banco;
    @Column(name="cuotas")
    private Integer cuotas;
    @Column(name="dniTitular")
    private String dniTitular;
    @Column(name="tipocambio")
    private String tipocambio;
    @Column(name="tipoMoneda")
    private  String tipoMoneda;

}
