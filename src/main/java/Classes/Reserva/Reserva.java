package Classes.Reserva;


import java.util.ArrayList;
import java.util.Date;

import Classes.Habitacion.Habitacion;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+
import java.util.Date;

@Entity // 1. Marca la clase como una tabla en la BDD
@Table(name = "reserva") // 2. (Opcional) Especifica el nombre de la tabla
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idReserva;


    private Date fechaDesde;
    private Date fechaHasta;
    private String estado;
    private String nombre;
    private String apellido;
    private int telefono;
    private ArrayList<Habitacion> listaHabitaciones= new ArrayList<>();
    
    public void cancelar(){

    }
    public void confirmar(){

    }   
}
