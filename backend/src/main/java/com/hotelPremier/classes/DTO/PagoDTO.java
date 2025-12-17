package com.hotelPremier.classes.DTO;

import java.util.Date;
import java.util.List;

import com.hotelPremier.classes.DTO.medioDePago.MedioDePagoDTO;

public class PagoDTO {

    private Integer id_pago;
    private float monto;
    private Date fecha;

    private FacturaDTO factura;               // Factura a la que aplica
    private List<MedioDePagoDTO> medios;      // Lista de medios

    public Integer getId_pago() { return id_pago; }
    public void setId_pago(Integer id_pago) { this.id_pago = id_pago; }

    public float getMonto() { return monto; }
    public void setMonto(float monto) { this.monto = monto; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public FacturaDTO getFactura() { return factura; }
    public void setFactura(FacturaDTO factura) { this.factura = factura; }

    public List<MedioDePagoDTO> getMedios() { return medios; }
    public void setMedios(List<MedioDePagoDTO> medios) { this.medios = medios; }
}
