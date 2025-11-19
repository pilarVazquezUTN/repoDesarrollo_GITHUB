package com.hotelPremier.classes.factura;

import java.util.Date;

public class FacturaDTO {
    private Date fecha;
    private float total;
    private String tipo;
    private String estado;

    /**
     * fecha
     * @return
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * sset de fecha
     * @param fecha
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * total monto
     * @return
     */
    public float getTotal() {
        return total;
    }

    /**
     * set de total monto
     * @param total
     */
    public void setTotal(float total) {
        this.total = total;
    }

    /**
     * tipo de factura
     * @return
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * set tipo factura
     * @param tipo
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * get estado de la factura
     * @return
     */
    public String getEstado() {
        return estado;
    }

    /**
     * set estado de la facutra
     * @param estado
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     *
     */
    public void agregarItem(){

    }

    /**
     *
     */
    public void cancelar(){

    }

    /**
     *
     */
    public void calcularTotal(){

    }
}
