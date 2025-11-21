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
    
    @Column(name="estado")
    private String estado;
    @Column(name="precio") 
    private float precio;
    @Column(name="cantidadpersonas") // comillas para respetar mayúscula
    private int cantidadPersonas;
    @Column(name="tipohabitacion")
    private String tipohabitacion; 
    @Column(name="camaskingsize")
    private Integer camasKingSize;
    @Column(name="camadoble")
    private Integer camaDoble;
    @Column(name="camasindividuales")
    private Integer camasIndividuales;



    public Integer getNumero() {
        return numero;
    }

    public String getTipohabitacion() {
        return  tipohabitacion ;
    }
} 
