package com.hotelPremier.classes.Dominio.medioDePago;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("MONEDA_EXTRANJERA")
public class MonedaExtranjera extends MedioDePago {

    @Column(name="tipo_moneda")
    private String tipoMoneda;

    public String getTipoMoneda() {
        return tipoMoneda;
    }

    public void setTipoMoneda(String tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
    }
}
