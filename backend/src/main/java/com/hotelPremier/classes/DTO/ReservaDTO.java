package com.hotelPremier.classes.DTO;

import java.util.Date;


public class ReservaDTO {

    private Integer id_reserva;
    private Date fecha_desde;
    private Date fecha_hasta;
    private String estado;
    private String nombre;
    private String apellido;
    private String telefono;

    private HabitacionDTO habitacion;  // <--- CAMBIO CRUCIAL

    // =======================
    // GETTERS / SETTERS
    // =======================

    public Integer getId_reserva() { return id_reserva; }
    public void setId_reserva(Integer id_reserva) { this.id_reserva = id_reserva; }

    public Date getFecha_desde() { return fecha_desde; }
    public void setFecha_desde(Date fecha_desde) { this.fecha_desde = fecha_desde; }

    public Date getFecha_hasta() { return fecha_hasta; }
    public void setFecha_hasta(Date fecha_hasta) { this.fecha_hasta = fecha_hasta; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public HabitacionDTO getHabitacion() { return habitacion; }
    public void setHabitacion(HabitacionDTO habitacion) { this.habitacion = habitacion; }
}
