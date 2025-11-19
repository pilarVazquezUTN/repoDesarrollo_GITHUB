package com.hotelPremier.classes.pago;

import java.util.Date;

public class PagoDTO {
    private float monto;
    private String medioPago;
    private Date  fecha;


    /**
     *
     * @return
     */
    public float getMonto() {
        return monto;
    }

    /**
     *
     * @param monto
     */
    public void setMonto(float monto) {
        this.monto = monto;
    }

    /**
     *
     * @return
     */
    public String getMedioPago() {
        return medioPago;
    }

    /**
     *
     * @param medioPago
     */
    public void setMedioPago(String medioPago) {
        this.medioPago = medioPago;
    }

    /**
     *
     * @return
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     *
     * @param fecha
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
