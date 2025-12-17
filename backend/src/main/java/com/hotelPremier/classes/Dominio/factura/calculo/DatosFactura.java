package com.hotelPremier.classes.Dominio.factura.calculo;

import com.hotelPremier.classes.Dominio.servicioExtra.ServicioExtra;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Clase de apoyo (NO entidad JPA) que contiene los datos necesarios
 * para el cálculo del total de una factura.
 */
public class DatosFactura {

    private List<ServicioExtra> consumosSeleccionados;
    private String tipoFactura; // "A" o "B"
    private LocalDateTime fechaHoraCheckoutReal;
    private Float totalEstimado; // Opcional, enviado por el frontend

    public DatosFactura() {
    }

    public DatosFactura(List<ServicioExtra> consumosSeleccionados,
                        String tipoFactura,
                        LocalDateTime fechaHoraCheckoutReal,
                        Float totalEstimado) {
        this.consumosSeleccionados = consumosSeleccionados;
        this.tipoFactura = tipoFactura;
        this.fechaHoraCheckoutReal = fechaHoraCheckoutReal;
        this.totalEstimado = totalEstimado;
    }

    public List<ServicioExtra> getConsumosSeleccionados() {
        return consumosSeleccionados;
    }

    public void setConsumosSeleccionados(List<ServicioExtra> consumosSeleccionados) {
        this.consumosSeleccionados = consumosSeleccionados;
    }

    public String getTipoFactura() {
        return tipoFactura;
    }

    public void setTipoFactura(String tipoFactura) {
        this.tipoFactura = tipoFactura;
    }

    public LocalDateTime getFechaHoraCheckoutReal() {
        return fechaHoraCheckoutReal;
    }

    public void setFechaHoraCheckoutReal(LocalDateTime fechaHoraCheckoutReal) {
        this.fechaHoraCheckoutReal = fechaHoraCheckoutReal;
    }

    public Float getTotalEstimado() {
        return totalEstimado;
    }

    public void setTotalEstimado(Float totalEstimado) {
        this.totalEstimado = totalEstimado;
    }
}

