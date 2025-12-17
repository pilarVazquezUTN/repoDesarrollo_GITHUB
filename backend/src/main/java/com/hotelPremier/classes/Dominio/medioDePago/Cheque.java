package com.hotelPremier.classes.Dominio.medioDePago;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@DiscriminatorValue("CHEQUE")
public class Cheque extends MedioDePago {

    @Column(name = "numero_cheque")
    private Integer numeroCheque;

    @Column(name = "banco")
    private String banco;

    @Temporal(TemporalType.DATE)
    @Column(name = "plazo")
    private Date plazo;

    public Integer getNumeroCheque() {
        return numeroCheque;
    }

    public void setNumeroCheque(Integer numeroCheque) {
        this.numeroCheque = numeroCheque;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public Date getPlazo() {
        return plazo;
    }

    public void setPlazo(Date plazo) {
        this.plazo = plazo;
    }
}
