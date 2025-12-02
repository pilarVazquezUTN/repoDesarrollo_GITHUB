package com.hotelPremier.classes.reserva;


import java.util.ArrayList;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hotelPremier.classes.estadia.Estadia;
import com.hotelPremier.classes.habitacion.Habitacion;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+
import java.util.Date;

@Entity
//@JsonIgnoreProperties("habitacion")
// 1. Marca la clase como una tabla en la BDD
@Table(name = "reserva") // 2. (Opcional) Especifica el nombre de la tabla
public class Reserva {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_reserva")
    private Integer id_reserva;

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
    @JsonIgnore
    @JoinColumn(name = "nro_habitacion") 
    private Habitacion nro_habitacion;

    @OneToOne
    @JoinColumn(name = "id_estadia")
    private Estadia estadia;


    public void setId_reserva(Integer id_reserva) {
        this.id_reserva = id_reserva;
    }
    public  Integer getId_reserva() {
        return this.id_reserva;
    }

    public void setHabitacion (Habitacion habitacion){

        this.nro_habitacion=habitacion;
    }
    public Habitacion getHabitacion(){
        return this.nro_habitacion;
    }

   /* public String getTipo_habitacion(){
        return nro_habitacion.getTipohabitacion();
    }
*/



    public String getNombre() {
        return this.nombre;
    }
   public void setNombre(String nombre) {
        this.nombre = nombre;
   }

    public void setFecha_desde(Date fecha_desde) {
        this.fecha_desde = fecha_desde;
    }

    public Date getFecha_desde() {
        return this.fecha_desde;
    }


    public void setApellido(String apellido) {
        this.apellido = apellido;
    }


    public String getApellido() {
        return this.apellido;
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
