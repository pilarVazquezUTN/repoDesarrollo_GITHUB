package com.hotelPremier.classes.Dominio.medioDePago;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("TARJETA_DEBITO")
public class TarjetaDebito extends MedioDePago {

    @Column(name="banco")
    private String banco;

    @Column(name="dni_titular")
    private String dniTitular;

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getDniTitular() {
        return dniTitular;
    }

    public void setDniTitular(String dniTitular) {
        this.dniTitular = dniTitular;
    }
}
