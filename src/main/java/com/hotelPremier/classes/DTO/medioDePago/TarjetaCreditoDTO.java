package com.hotelPremier.classes.DTO.medioDePago;

public class TarjetaCreditoDTO {
    private String banco;
    private int cuotas;

    /**
     *
     * @param banco
     */
    public void setBanco(String banco) {
        this.banco = banco;
    }

    /**
     *
     * @return
     */
    public String getBanco() {

        return banco;

    }

    /**
     *
     * @param cuotas
     */
    public void setCuotas(int cuotas) {
        this.cuotas = cuotas;
    }

    /**
     *
     * @return
     */
    public int getCuotas() {
        return cuotas;
    }    
}
