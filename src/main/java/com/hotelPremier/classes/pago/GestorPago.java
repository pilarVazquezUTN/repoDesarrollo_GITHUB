package com.hotelPremier.classes.pago;

import com.hotelPremier.classes.factura.Factura;

public class GestorPago {
    private String idEmpleado;

    /**
     *
     * @param idEmpleado
     */
    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    /**
     *
     * @return
     */
    public String getIdEmpleado() {
        return idEmpleado;
    }

    /**
     *
     * @param factura
     * @param monto
     */
    public void ingresarPago(Factura factura,  float monto){

    }

    /**
     *
     * @return
     */
    public boolean validarMonto(){
        return false;
    }

    /**
     *
     */
    public void registrarPago(){

    }

    /**
     *
     * @return
     */
    public boolean errorValidacion(){
        return false;
    }
}
