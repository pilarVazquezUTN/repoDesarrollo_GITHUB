package com.hotelPremier.classes.DTO;

import java.util.List;

public class NotaDeCreditoDTO {
    private Integer id;
    private Float importe;       // valor de la NC
    private List<FacturaDTO> listaFacturas;

    public Float getImporte() { return importe; }
    public void setImporte(Float importe) { this.importe = importe; }

    public Integer getID() { return id; }

    public List<FacturaDTO> getFacturas(){
        return this.listaFacturas;
    }

    public void setFacturas(List<FacturaDTO> facturas){
        this.listaFacturas = facturas;
    }

}
