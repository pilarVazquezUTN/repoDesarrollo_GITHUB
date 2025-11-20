package com.hotelPremier.classes.habitacion;

import java.util.ArrayList;

import com.hotelPremier.classes.reserva.Reserva;
import com.hotelPremier.classes.estadia.Estadia;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+

@Entity
@Table(name="habitacion")
public class Habitacion  {
    @Id 
    private int numero;
    private String estado;
    private float precio;
    private int cantidadPersonas;
    private String tipohabitacion;
    private Integer camasKingSize;
    private Integer camaDoble;
    private Integer camasIndividuales;
} 
