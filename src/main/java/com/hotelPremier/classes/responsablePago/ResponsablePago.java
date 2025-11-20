package com.hotelPremier.classes.responsablePago;

import com.hotelPremier.classes.direccion.Direccion;
import com.hotelPremier.classes.factura.Factura;
import com.hotelPremier.classes.huesped.Huesped;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+

@Entity
@Table(name="\"responsablePago\"")
public class ResponsablePago {

    @Id
    @Column(name="\"id_responsablePago\"")
    private Integer id_responsablePago; 
    
    @Column(name="cuit")
    private String cuit;
    @Column(name="\"razonSocial\"")
    private String razonSocial;
    @Column(name="nombre")
    private String nombre;
    @Column(name="\"tipoPersona\"")
    private String tipoPersona;


    @OneToOne
    @JoinColumn(name = "id_direccion" )
    private Direccion direccion;


    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "tipoDoc", referencedColumnName = "tipoDocumento"),
            @JoinColumn(name = "dni", referencedColumnName = "dni")
    })
    private Huesped huesped;

    @ManyToOne
    @JoinColumn(name = "nro_factura")
    private Factura nro_factura;
}
