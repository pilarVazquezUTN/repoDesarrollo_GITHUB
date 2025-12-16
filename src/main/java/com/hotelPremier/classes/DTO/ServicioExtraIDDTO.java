package com.hotelPremier.classes.DTO;

public class ServicioExtraIDDTO {

    private Integer id_servicio;
    private Integer id_estadia;

    public ServicioExtraIDDTO() {}

    public ServicioExtraIDDTO(Integer id_servicio, Integer id_estadia) {
        this.id_servicio = id_servicio;
        this.id_estadia = id_estadia;
    }

    public Integer getId_servicio() {
        return id_servicio;
    }

    public void setId_servicio(Integer id_servicio) {
        this.id_servicio = id_servicio;
    }

    public Integer getId_estadia() {
        return id_estadia;
    }

    public void setId_estadia(Integer id_estadia) {
        this.id_estadia = id_estadia;
    }
}
