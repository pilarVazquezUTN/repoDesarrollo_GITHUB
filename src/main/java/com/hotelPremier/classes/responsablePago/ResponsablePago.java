package com.hotelPremier.classes.responsablePago;

import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+

@Entity
@Table(name="responsablePago")
public class ResponsablePago {
    @Id
    private Integer id_responsablePago; 
    
    
    private String cuit;
    private String razonSocial;
    private String nombre;
    private String tipoPersona;
    @OneToOne
    private Integer id_direccion;
    @OneToOne 
    private String tipoDoc;
    @OneToOne
    private String dni;
    @ManyToOne
    private String nro_factura;
}
