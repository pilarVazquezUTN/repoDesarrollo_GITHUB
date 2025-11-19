package Classes.Habitacion;

import java.util.ArrayList;

import Classes.Reserva.Reserva;
import Classes.Estadia.Estadia;
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
