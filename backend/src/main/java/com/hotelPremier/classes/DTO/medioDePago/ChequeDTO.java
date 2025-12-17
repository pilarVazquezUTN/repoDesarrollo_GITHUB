package com.hotelPremier.classes.DTO.medioDePago;

import java.util.Date;

public class ChequeDTO extends MedioDePagoDTO {

    private int numeroCheque;
    private String banco;
    private Date plazo;

    public int getNumeroCheque() { return numeroCheque; }
    public void setNumeroCheque(int numeroCheque) { this.numeroCheque = numeroCheque; }

    public String getBanco() { return banco; }
    public void setBanco(String banco) { this.banco = banco; }

    public Date getPlazo() { return plazo; }
    public void setPlazo(Date plazo) { this.plazo = plazo; }
}
