package Classes.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;


@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {


    List<Reserva> findByApellidoContainingIgnoreCase(String apellido);
}
