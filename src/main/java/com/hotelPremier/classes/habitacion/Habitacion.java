package com.hotelPremier.classes.habitacion;

 import java.util.List;

import com.hotelPremier.classes.reserva.Reserva;
import com.hotelPremier.classes.estadia.Estadia;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+

@Entity
@Table(name="habitacion")
public class Habitacion  {
    @Id 
    private int numero; 
    
    @Column(name="estado") 
    private String estado;
    @Column(name="precio")  
    private float precio;
    @Column(name="cantidadpersonas") // comillas para respetar mayúsculaaaaaaaaaa
    private int cantidadPersonas;
    
    private List<Estadia> listaestadias;



    public Integer getNumero() {
        return numero;
    }
} 
