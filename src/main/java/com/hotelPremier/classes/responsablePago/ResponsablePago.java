package com.hotelPremier.classes.responsablePago;

import java.util.List;

import com.hotelPremier.classes.direccion.Direccion;
import com.hotelPremier.classes.factura.Factura;
import com.hotelPremier.classes.huesped.Huesped;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+

@Entity
@Table(name="responsablepago")
public class ResponsablePago {

    @Id
    @Column(name="id_responsablepago")
    private Integer id_responsablePago; 
    
    /* 
    @Column(name="cuit")
    private String cuit;
    @Column(name="razonsocial")
    private String razonSocial;
    @Column(name="nombre")
    private String nombre;
    @Column(name="tipopersona")
    private String tipoPersona;
    */
    


    @OneToOne
    @JoinColumn(name = "id_direccion" )
    private Direccion direccion;


    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "tipodoc", referencedColumnName = "tipodocumento"),
            @JoinColumn(name = "dni", referencedColumnName = "dni")
    })
    private Huesped huesped;

    @ManyToOne
    @JoinColumn(name = "nro_factura")
    private Factura nro_factura;

}
