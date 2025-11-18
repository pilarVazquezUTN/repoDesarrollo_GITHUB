package Classes.Usuario;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+

@Entity
@Table(name="usuario")
public class Usuario {
    String nombre;
    String contraseña;
}
