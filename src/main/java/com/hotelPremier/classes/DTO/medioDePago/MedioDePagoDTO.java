package com.hotelPremier.classes.DTO.medioDePago;

import java.util.Date;
import java.util.List;
import com.hotelPremier.classes.DTO.PagoDTO;

public class MedioDePagoDTO {

    private Integer id_mediodepago;
    private int monto;
    private Date fecha;
    private List<PagoDTO> listapago;

    // Constructor vacío
    public MedioDePagoDTO() {
    }

    // Constructor con parámetros
    public MedioDePagoDTO(Integer id_mediodepago, int monto, Date fecha, List<PagoDTO> listapago) {
        this.id_mediodepago = id_mediodepago;
        this.monto = monto;
        this.fecha = fecha;
        this.listapago = listapago;
    }

    // Getters y Setters

    public Integer getId_mediodepago() {
        return id_mediodepago;
    }

    public void setId_mediodepago(Integer id_mediodepago) {
        this.id_mediodepago = id_mediodepago;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List<PagoDTO> getListapago() {
        return listapago;
    }

    public void setListapago(List<PagoDTO> listapago) {
        this.listapago = listapago;
    }

}
