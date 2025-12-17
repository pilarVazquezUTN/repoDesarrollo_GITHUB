package com.hotelPremier.classes.DTO.medioDePago;

public class MonedaExtranjeraDTO extends MedioDePagoDTO {

    private String tipoMoneda;
    private Double cotizacion;
    
    public String getTipoMoneda() { return tipoMoneda; }
    public void setTipoMoneda(String tipoMoneda) { this.tipoMoneda = tipoMoneda; }
    
    public Double getCotizacion() { return cotizacion; }
    public void setCotizacion(Double cotizacion) { this.cotizacion = cotizacion; }
}
