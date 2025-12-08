package com.hotelPremier.classes.Dominio.responsablePago;

import com.hotelPremier.classes.Dominio.Direccion;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

public class PersonaJuridica extends ResponsablePago{
    private String razonSocial;
    private String cuit;
    private Direccion direccion;

    @OneToOne
    @JoinColumn(name = "id_direccion")
    Direccion direccionpersonaJuridica;

}
