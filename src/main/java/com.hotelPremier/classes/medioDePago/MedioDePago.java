package classes.medioDePago;

import java.util.Date;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+

@Entity
@Table(name="medio_pago")
public class MedioDePago {
    private int monto;
    private Date fecha;
}
