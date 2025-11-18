package Classes.Estadia;

import java.util.Date;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+

@Entity
@Table(name="estadia")
public class Estadia {
    private Date checkin;
    private Date checkout;
}