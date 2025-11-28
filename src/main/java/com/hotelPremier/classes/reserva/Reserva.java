package com.hotelPremier.classes.reserva;


import java.util.ArrayList;
import java.util.Date;

import com.hotelPremier.classes.habitacion.Habitacion;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+
import java.util.Date;

@Entity // 1. Marca la clase como una tabla en la BDD
@Table(name = "reserva") // 2. (Opcional) Especifica el nombre de la tabla
public class Reserva {
    @Id
    @Column(name="id_reserva")
    private int id_reserva;

    @Temporal(TemporalType.DATE) //guarda fecha sin hora
    @Column(name="fecha_desde")
    private Date fecha_desde;

    @Temporal(TemporalType.DATE) //guarda fecha sin hora
    @Column(name="fecha_hasta") 
    private Date fecha_hasta;
    
    @Column(name="estado")
    private String estado;
    @Column(name="nombre")
    private String nombre;
    @Column(name="apellido")
    private String apellido;
    @Column(name="telefono")
    private String telefono;

    @ManyToOne
    @JoinColumn(name = "nro_habitacion")
    private Habitacion nro_habitacion;


    

    public void setHabitacion (Habitacion habitacion){

        nro_habitacion=habitacion;
    }

   /* public String getTipo_habitacion(){
        return nro_habitacion.getTipohabitacion();
    }
*/

    public void setNro_habitacion(Habitacion nro_habitacion) {
        this.nro_habitacion = nro_habitacion;
    }


    public Habitacion getNro_habitacion(){
        return nro_habitacion;
    }

    public String getNombre() {
        return nombre;
    }
   public void setNombre(String nombre) {
        this.nombre = nombre;
   }

    public void setFecha_desde(Date fecha_desde) {
        this.fecha_desde = fecha_desde;
    }

    public Date getFecha_desde() {
        return fecha_desde;
    }


    public void setApellido(String apellido) {
        this.apellido = apellido;
    }


    public String getApellido() {
        return apellido;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    public void setFecha_hasta(Date fecha_hasta) {
        this.fecha_hasta = fecha_hasta;
    }

    public Date getFecha_hasta() {
        return fecha_hasta;
    }

}
