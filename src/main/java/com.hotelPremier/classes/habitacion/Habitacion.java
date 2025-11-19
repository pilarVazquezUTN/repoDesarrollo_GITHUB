package classes.habitacion;

import java.util.ArrayList;

import classes.reserva.Reserva;
import classes.estadia.Estadia;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+

@Entity
@Table(name="habitacion")
public abstract class Habitacion  {
    private int numero;
    private String estado;
    private float precio;
    private int cantidadPersonas;
    private ArrayList<Reserva> listaReservas= new ArrayList<>();
    private ArrayList<Estadia> listaEstadias= new ArrayList<>();
} 
