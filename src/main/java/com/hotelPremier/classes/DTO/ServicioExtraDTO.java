package com.hotelPremier.classes.DTO;

public class ServicioExtraDTO {

    private ServicioExtraIDDTO servicioExtraID;
    private String tipo_servicio;
    private Float precio;

    public ServicioExtraDTO() {
        this.servicioExtraID = new ServicioExtraIDDTO();
    }

    public ServicioExtraIDDTO getServicioExtraID() {
        return servicioExtraID;
    }

    public void setServicioExtraID(ServicioExtraIDDTO servicioExtraID) {
        this.servicioExtraID = servicioExtraID;
    }

    public String getTipo_servicio() {
        return tipo_servicio;
    }

    public void setTipo_servicio(String tipo_servicio) {
        this.tipo_servicio = tipo_servicio;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }
}
