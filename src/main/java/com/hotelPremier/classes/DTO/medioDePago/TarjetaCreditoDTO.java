package com.hotelPremier.classes.DTO.medioDePago;

public class TarjetaCreditoDTO extends MedioDePagoDTO {

    private String banco;
    private int cuotas;

    public String getBanco() { return banco; }
    public void setBanco(String banco) { this.banco = banco; }

    public int getCuotas() { return cuotas; }
    public void setCuotas(int cuotas) { this.cuotas = cuotas; }
}
