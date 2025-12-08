package com.hotelPremier.classes.Dominio.responsablePago;

import com.hotelPremier.classes.Dominio.Huesped;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.OneToOne;

public class PersonaFisica extends ResponsablePago{
    private String nombre;


    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "dni", referencedColumnName = "dni"),
            @JoinColumn(name = "tipodocumento", referencedColumnName = "tipodocumento")
    })
    private Huesped huesped;
}
