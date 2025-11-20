package com.hotelPremier.classes.responsablePago;

import com.hotelPremier.classes.direccion.Direccion;
import com.hotelPremier.classes.factura.Factura;
import com.hotelPremier.classes.huesped.Huesped;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+

@Entity
@Table(name="responsablepago")
public class ResponsablePago {

    @Id
    private Integer id_responsablePago; 
    
    
    private String cuit;
    private String razonSocial;
    private String nombre;
    private String tipoPersona;


    @OneToOne
    @JoinColumn(name = "id_direccion" )
    private Direccion direccion;


    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "tipoDocumento", referencedColumnName = "tipoDocumento"),
            @JoinColumn(name = "dni", referencedColumnName = "numeroDocumento")
    })
    private Huesped huesped;

    @ManyToOne
    @JoinColumn(name = "nro_factura")
    private Factura nro_factura;
}
