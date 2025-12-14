package com.hotelPremier.classes.DTO;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class FacturaDTO {

    private Integer id;
    private Date fecha;
    private float total;
    private String tipo;
    private String estado;
    private EstadiaDTO estadia;
    private NotaDeCreditoDTO notacredito;
    private PagoDTO pago;
    private ResponsablePagoDTO responsablepago;   // 👈 AHORA ES DTO
    
    // Campos opcionales para cálculo automático con Strategy
    private List<Integer> consumosIds; // IDs de los servicios extra seleccionados
    private LocalDateTime fechaHoraCheckoutReal; // Fecha y hora real del checkout
    private Float totalEstimado; // Total estimado enviado por el frontend (para validación)

    public FacturaDTO() {
    }

    public FacturaDTO(Integer id,
                      Date fecha,
                      float total,
                      String tipo,
                      String estado,
                      EstadiaDTO estadia,
                      NotaDeCreditoDTO notacredito,
                      PagoDTO pago,
                      ResponsablePagoDTO responsablepago) {
        this.id = id;
        this.fecha = fecha;
        this.total = total;
        this.tipo = tipo;
        this.estado = estado;
        this.estadia = estadia;
        this.notacredito = notacredito;
        this.pago = pago;
        this.responsablepago = responsablepago;
    }

    public Integer getID() {
        return id;
    }

    public void setID(Integer id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public EstadiaDTO getEstadia() {
        return estadia;
    }

    public void setEstadia(EstadiaDTO estadia) {
        this.estadia = estadia;
    }

    public NotaDeCreditoDTO getNotacredito() {
        return notacredito;
    }

    public void setNotacredito(NotaDeCreditoDTO notacredito) {
        this.notacredito = notacredito;
    }

    public PagoDTO getPago() {
        return pago;
    }

    public void setPago(PagoDTO pago) {
        this.pago = pago;
    }

    public ResponsablePagoDTO getResponsablepago() {
        return responsablepago;
    }

    public void setResponsablepago(ResponsablePagoDTO responsablepago) {
        this.responsablepago = responsablepago;
    }

    // Getters y setters para campos de cálculo
    public List<Integer> getConsumosIds() {
        return consumosIds;
    }

    public void setConsumosIds(List<Integer> consumosIds) {
        this.consumosIds = consumosIds;
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
