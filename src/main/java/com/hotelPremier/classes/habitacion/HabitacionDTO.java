package com.hotelPremier.classes.habitacion;

import com.hotelPremier.classes.estadia.Estadia;

import com.hotelPremier.classes.estadia.EstadiaDTO;
import com.hotelPremier.classes.reserva.Reserva;
import com.hotelPremier.classes.reserva.ReservaDTO;

import java.util.List;

public class HabitacionDTO {

    private Integer numero;
    private String estado;
    private float precio;
    private Integer cantidadPersonas;
    private String tipohabitacion;// cuando mapea con los DTO se le asigna un tipo.
    private List<EstadiaDTO> listaestadias;
    private List<ReservaDTO> listareservas;
    


    // GETTERS y SETTERS

    public HabitacionDTO(){}
    public  HabitacionDTO(Habitacion  habitacion, List<ReservaDTO> reservas, List<EstadiaDTO> estadias

    ){
        this.numero=habitacion.getNumero();
        this.estado=habitacion.getEstado();
        this.precio=habitacion.getPrecio();
        this.cantidadPersonas=habitacion.getCantidadPersonas();
        this.tipohabitacion=habitacion.getTipo();
        this.listareservas = reservas;
        this.listaestadias = estadias;

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

    //public void setListaestadias( List<EstadiaDTO> listaestadias) {this.listaestadias= listaestadias;}
    public List<EstadiaDTO> getListaestadias() { return this.listaestadias; }
    public void setListareservas(List<ReservaDTO> listares){
        this.listareservas=listares;
    }
    public List<ReservaDTO> getListareservas() { return this.listareservas; }
 
}
