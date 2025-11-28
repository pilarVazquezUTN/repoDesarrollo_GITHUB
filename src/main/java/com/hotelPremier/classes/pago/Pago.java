package com.hotelPremier.classes.pago;

import java.util.Date;
import java.util.List;

import com.hotelPremier.classes.medioDePago.MedioDePago;

import com.hotelPremier.classes.factura.Factura;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+

@Entity
@Table(name="pago")
public class Pago {
    @Id
    @Column(name="id_pago")
    private Integer id_pago;
    @Column(name="monto")
    private float monto;
    @Column(name="mediopago")
    private String medioPago;
    @Column(name="fecha")
    private Date  fecha;

    @OneToOne
    @JoinColumn(name = "factura")
    private Factura factura;


    @ManyToMany
    @JoinTable(
        name="mediopago_pago",
        joinColumns = @JoinColumn(name= "id_pago"),
        inverseJoinColumns = @JoinColumn(name= "id_mediodepago")
    )
    private List<MedioDePago> listamediodepago;
}
