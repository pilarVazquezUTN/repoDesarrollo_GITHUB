package com.hotelPremier.classes.Dominio.medioDePago;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("TARJETA_CREDITO")
public class TarjetaCredito extends MedioDePago {

    @Column(name = "banco")
    private String banco;

    @Column(name = "cuotas")
    private Integer cuotas;

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public Integer getCuotas() {
        return cuotas;
    }

    public void setCuotas(Integer cuotas) {
        this.cuotas = cuotas;
    }
}
