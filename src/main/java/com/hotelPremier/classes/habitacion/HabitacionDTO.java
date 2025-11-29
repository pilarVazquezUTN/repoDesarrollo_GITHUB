package com.hotelPremier.classes.habitacion;

import com.hotelPremier.classes.estadia.Estadia;

import com.hotelPremier.classes.estadia.EstadiaDTO;

import java.util.List;

public class HabitacionDTO {

    private Integer numero;
    private String estado;
    private float precio;
    private Integer cantidadPersonas;
    private String tipohabitacion;// cuando mapea con los DTO se le asigna un tipo.
    private List<EstadiaDTO> listaestadias;

    // GETTERS y SETTERS

    public void setTipohabitacion(String tipoHab) {
        this.tipohabitacion = tipoHab;
    }
    public String getTipohabitacion() {
        return tipohabitacion;
    }

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public float getPrecio() { return precio; }
    public void setPrecio(float precio) { this.precio = precio; }

    public int getCantidadPersonas() { return cantidadPersonas; }
    public void setCantidadPersonas(int cantidadPersonas) { this.cantidadPersonas = cantidadPersonas; }
}
