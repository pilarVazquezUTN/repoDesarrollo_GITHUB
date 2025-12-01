package com.hotelPremier.classes.reserva;
import java.util.Date;

import com.hotelPremier.classes.habitacion.HabitacionDTO;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;


public class ReservaDTO {
    @Temporal(TemporalType.DATE) //guarda fecha sin hora
    private Date fecha_desde;

    @Temporal(TemporalType.DATE) //guarda fecha sin hora
    private Date fecha_hasta;

    private Integer id_reserva;
    private String estado;
    private String nombre;
    private String apellido;
    private String telefono;
    private HabitacionDTO habitacion; 
    //private String tipoHab;
 
    public void setHabitacion(HabitacionDTO hab) {

        this.habitacion = hab;
    }

    public HabitacionDTO getHabitacion() {
        return this.habitacion;
    }

    /* 
    public void setTipoHab(String tipoHab) {
        this.tipoHab = tipoHab;

    }

    public String getTipoHab() {
        return tipoHab;
    }
    */
    public void setId_reserva(Integer id_reserva) {
        this.id_reserva = id_reserva;
    }
    public Integer getId_reserva() {
        return this.id_reserva;
    }

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
    public Date getFecha_desde() {
        return fecha_desde;
    }

    /**
     *
     * @param fechaDesde
     */
    public void setFecha_desde(Date fechaDesde) {
        this.fecha_desde = fechaDesde;
    }

    /**
     *
     * @return
     */
    public Date getFecha_hasta() {
        return fecha_hasta;
    }

    /**
     *
     * @param fechaHasta
     */
    public void setFecha_hasta(Date fechaHasta) {
        this.fecha_hasta = fechaHasta;
    }
}
