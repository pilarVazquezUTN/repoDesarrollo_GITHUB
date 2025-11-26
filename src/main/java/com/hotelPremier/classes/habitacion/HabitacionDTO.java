package com.hotelPremier.classes.habitacion;

import com.hotelPremier.classes.estadia.Estadia;
import java.util.List;

public class HabitacionDTO {

    private int numero;


    private String estado;

    private float precio;

    private int cantidadPersonas;

    private String tipoHab;

    // GETTERS y SETTERS

    public void setTipoHab(String tipoHab) {
        this.tipoHab = tipoHab;
    }
    public String getTipoHab() {
        return tipoHab;
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
