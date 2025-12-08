package com.hotelPremier.classes.Dominio.responsablePago;

import java.util.List;

import com.hotelPremier.classes.Dominio.Direccion;
import com.hotelPremier.classes.Dominio.Factura;
import com.hotelPremier.classes.Dominio.Huesped;

import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+

@Entity
@Table(name="responsablepago")
public class ResponsablePago {

    @Id
    @Column(name="id_responsablepago")
    private Integer id_responsablePago; 


    @OneToOne
    @JoinColumn(name = "id_direccion" )
    private Direccion direccion;


    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "tipodoc", referencedColumnName = "tipodocumento"),
            @JoinColumn(name = "dni", referencedColumnName = "dni")
    })
    private Huesped huesped;
    
    @OneToMany(mappedBy = "responsablepago")
    private List<Factura>  listafactura;

}
