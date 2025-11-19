package com.hotelPremier.classes.medioDePago;

public class TarjetaDebitoDTO {
    private String banco;
    private String dniTitular;

    /**
     *
     * @return
     */
    public String getBanco() {

        return banco;
    }

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
    public String getDniTitular() {
        return dniTitular;
    }

    /**
     *
     * @param dniTitular
     */
    public void setDniTitular(String dniTitular) {
        this.dniTitular = dniTitular;
    }
}
