package com.hotelPremier.classes.Dominio.responsablePago;

import com.hotelPremier.classes.Dominio.Huesped;

import jakarta.persistence.*;

@Entity
@Table(name = "persona_fisica")
public class PersonaFisica extends ResponsablePago {

    @OneToOne
    @JoinColumns({
        @JoinColumn(name = "dni", referencedColumnName = "dni"),
        @JoinColumn(name = "tipodocumento", referencedColumnName = "tipodocumento")
    })
    private Huesped huesped;

    public Huesped getHuesped() { return huesped; }
    public void setHuesped(Huesped huesped) { this.huesped = huesped; }
}
