package com.hotelPremier.classes.usuario;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+

@Entity
@Table(name="usuario")
public class Usuario {
    @Id 
    String nombre;
    String contrasenia;
}
