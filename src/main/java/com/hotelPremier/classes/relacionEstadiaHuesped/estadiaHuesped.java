package com.hotelPremier.classes.relacionEstadiaHuesped;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="estadiaHuesped")
public class estadiaHuesped {
    @Id 
    @ManyToOne
    private Integer id_estadia;
    @Id 
    @ManyToOne
    private String dni;
    @Id 
    @ManyToOne
    private String tipoDocumento;
}
