package com.hotelPremier.classes.Dominio.servicioExtra;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ServicioExtraID implements Serializable {
    @Column(name="id_servicio")
    private Integer id_servicio;
    @Column(name="id_estadia")
    private Integer id_estadia;

    public void setId_estadia(Integer id_estadia) {
        this.id_estadia = id_estadia;
    }
    
    public Integer getId_estadia() {
        return id_estadia;
    }
    public void setId_servicio(Integer id_servicio) {
        this.id_servicio = id_servicio;
    }
    public Integer getId_servicio() {
        return id_servicio;
    }

    // equals y hashCode obligatorios
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServicioExtraID)) return false;
        ServicioExtraID that = (ServicioExtraID) o;
        return Objects.equals(id_servicio, that.id_servicio) &&
               Objects.equals(id_estadia, that.id_estadia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_servicio, id_estadia);
    }
} 
