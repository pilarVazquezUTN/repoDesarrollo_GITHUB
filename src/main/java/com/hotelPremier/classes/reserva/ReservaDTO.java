package com.hotelPremier.classes.reserva;
import java.util.Date;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;


public class ReservaDTO {
    @Temporal(TemporalType.DATE) //guarda fecha sin hora
    private Date fecha_desde;

    @Temporal(TemporalType.DATE) //guarda fecha sin hora
    private Date fecha_hasta;

    private String estado;
    private String nombre;
    private String apellido;
    private String telefono;
    private Integer nro_habitacion;
    //private String tipoHab;
 
    public void setNumeroHabitacion(int numeroHab) {

        this.nro_habitacion = numeroHab;
    }

    public int getNumeroHabitacion() {
        return nro_habitacion;
    }

    /* 
    public void setTipoHab(String tipoHab) {
        this.tipoHab = tipoHab;

    }

    public String getTipoHab() {
        return tipoHab;
    }
    */


    /**
     *
     * @param telefono
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     *
     * @return
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     *
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     *
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     *
     * @param apellido
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     *
     * @return
     */
    public String getApellido() {
        return apellido;
    }

    /**
     *
     * @param estado
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     *
     * @return
     */
    public String getEstado() {
        return estado;
    }

    /**
     *
     * @return
     */
    public Date getFechaDesde() {
        return fecha_desde;
    }

    /**
     *
     * @param fechaDesde
     */
    public void setFechaDesde(Date fechaDesde) {
        this.fecha_desde = fechaDesde;
    }

    /**
     *
     * @return
     */
    public Date getFechaHasta() {
        return fecha_hasta;
    }

    /**
     *
     * @param fechaHasta
     */
    public void setFechaHasta(Date fechaHasta) {
        this.fecha_hasta = fechaHasta;
    }
}
