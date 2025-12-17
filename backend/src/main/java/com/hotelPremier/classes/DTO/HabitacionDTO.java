package com.hotelPremier.classes.DTO;

import com.hotelPremier.classes.Dominio.Habitacion;

import java.util.List;

public class HabitacionDTO {

    private Integer numero;
    private String estado;
    private float precio;
    private Integer cantidadPersonas;
    private String tipohabitacion;


    // GETTERS y SETTERS

    public HabitacionDTO(){}
    public  HabitacionDTO(Habitacion  habitacion, List<ReservaDTO> reservas, List<EstadiaDTO> estadias

    ){
        this.numero=habitacion.getNumero();
        this.estado=habitacion.getEstado();
        this.precio=habitacion.getPrecio();
        this.cantidadPersonas=habitacion.getCantidadPersonas();
        this.tipohabitacion=habitacion.getTipo();

    }

    public void setTipohabitacion(String tipoHab) { this.tipohabitacion = tipoHab; }
    public String getTipohabitacion() {return this.tipohabitacion; }

    public Integer getNumero() { return this.numero; }
    public void setNumero(Integer numero) { this.numero = numero; }

    public String getEstado() { return this.estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public float getPrecio() { return this.precio; }
    public void setPrecio(float precio) { this.precio = precio; }

    public int getCantidadPersonas() { return this.cantidadPersonas; }
    public void setCantidadPersonas(int cantidadPersonas) { this.cantidadPersonas = cantidadPersonas; }

 
}
