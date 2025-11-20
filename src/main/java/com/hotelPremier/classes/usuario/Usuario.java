package com.hotelPremier.classes.usuario;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+

@Entity
@Table(name="usuario")
public class Usuario {
    @Id 
    @Column(name="nombre")
    String nombre;
    @Column(name="contrasenia")
    String contrasenia;
}
