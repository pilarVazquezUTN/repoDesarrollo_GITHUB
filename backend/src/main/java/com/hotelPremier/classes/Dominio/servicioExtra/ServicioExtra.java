package com.hotelPremier.classes.Dominio.servicioExtra;

import com.hotelPremier.classes.Dominio.Estadia;

import jakarta.persistence.*;

@Entity
@Table(name="servicio_extra")
public class ServicioExtra {


    @EmbeddedId
    private ServicioExtraID servicioExtraID;


    @ManyToOne
    @JoinColumn(name = "id_estadia",insertable = false, updatable = false)
    private Estadia estadia; // La Entidad completa

    @Column(name="tipo_servicio")
    private String tipo_servicio ;
    @Column(name="precio")
    private Float precio ;

    // GETTERS & SETTERS
    public ServicioExtraID getServicioExtraID() {
        return servicioExtraID;
    }

    public void setServicioExtraID(ServicioExtraID servicioExtraID) {
        this.servicioExtraID = servicioExtraID;
    }

    public Estadia getEstadia() {
        return estadia;
    }

    public void setEstadia(Estadia estadia) {
        this.estadia = estadia;
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
