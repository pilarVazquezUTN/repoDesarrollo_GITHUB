package classes.reserva;


import java.util.ArrayList;
import java.util.Date;

import classes.habitacion.habitacion;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+
import java.util.Date;

@Entity // 1. Marca la clase como una tabla en la BDD
@Table(name = "reserva") // 2. (Opcional) Especifica el nombre de la tabla
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //manejo automatico de el id, incremental
    private int id_reserva;


    private Date fecha_desde;

    private Date fecha_hasta;

    private String estado;

    private String nombre;

    private String apellido;

    private int telefono;

    private int nro_habitacion ;
    
    public void cancelar(){

    }
    public void confirmar(){

    }

    public String getNombre() {
        return nombre;
    }

    public Date getFecha_desde() {
        return fecha_desde;
    }

    public int getNro_habitacion() {
        return nro_habitacion;
    }

    public String getApellido() {
        return apellido;
    }
    public int getTelefono() {
        return telefono;
    }

    public String getEstado() {
        return estado;
    }
    public Date getFecha_hasta() {
        return fecha_hasta;
    }

}
