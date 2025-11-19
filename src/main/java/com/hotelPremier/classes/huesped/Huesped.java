package com.hotelPremier.classes.huesped;
//import Classes.Estadia.*;
import java.util.Calendar;
import java.util.Date;

import com.hotelPremier.classes.direccion.DireccionDTO;

import com.hotelPremier.classes.estadia.Estadia;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+

@Entity
@Table(name = "huesped") // 2. (Opcional) Especifica el nombre de la tabla
public class Huesped {
    private String apellido;
    private String nombre;
    @Id
    private String tipoDocumento;
    @Id
    private String numeroDocumento;
    private Date fechaNacimiento;
    private String telefono;
    private String email;
    private DireccionDTO direccionHuesped;
    private String cuit;
    private String posicionIva;
    private String ocupacion;
    private String nacionalidad;
    private Estadia [] estadiaHuesped;

    

}


