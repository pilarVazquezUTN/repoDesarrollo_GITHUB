package Classes.Huesped;
//import Classes.Estadia.*;
import java.util.Calendar;
import java.util.Date;

import Classes.Direccion.DireccionDTO;
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
    private Classes.Estadia.Estadia [] estadiaHuesped;

    

}


