package com.hotelPremier.classes.reserva;
import java.util.Date;

import com.hotelPremier.classes.habitacion.HabitacionDTO;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.criteria.CriteriaBuilder;


public class ReservaDTO {
//ERNE
    private Integer id_reserva;

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
 
     //ERNE
    public void setId_reserva(Integer id_reserva) { this.id_reserva = id_reserva; }
    public Integer getId_reserva() { return this.id_reserva; }

    public void setNro_habitacion(Integer  nro_hab) {

        this.nro_habitacion = nro_hab;
    }

    public Integer getNro_habitacion() {
        return this.nro_habitacion;
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
