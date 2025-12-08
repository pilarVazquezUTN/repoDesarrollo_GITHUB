package com.hotelPremier.classes.DTO;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

public class PagoDTO {

    private Integer id_pago;
    private float monto;
    private String medioPago;
    private Date fecha;
    private FacturaDTO factura;
    // ===========================
    // Constructores
    // ===========================

    public PagoDTO() {
    }

    public PagoDTO(Integer id_pago, float monto, String medioPago, Date fecha,
                FacturaDTO factura) {
        this.id_pago = id_pago;
        this.monto = monto;
        this.medioPago = medioPago;
        this.fecha = fecha;
        this.factura = factura;
    }

    // ===========================
    // Getters y Setters
    // ===========================

    public Integer getId_pago() {
        return id_pago;
    }

    public void setId_pago(Integer id_pago) {
        this.id_pago = id_pago;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public String getMedioPago() {
        return medioPago;
    }

    public void setMedioPago(String medioPago) {
        this.medioPago = medioPago;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public FacturaDTO getFactura() {
        return factura;
    }

    public void setFactura(FacturaDTO factura) {
        this.factura = factura;
    }


}
