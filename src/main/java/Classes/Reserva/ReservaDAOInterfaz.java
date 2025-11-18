package Classes.Reserva;

import java.util.List;

public interface ReservaDAOInterfaz {
    void delete();
    void create();
    void update();
    List<ReservaDTO> read(String apellido); //si apellido es null , devuelve todos
}
