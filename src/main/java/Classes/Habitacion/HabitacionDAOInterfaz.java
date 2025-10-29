package Classes.Habitacion;

import java.util.Date;

public interface HabitacionDAOInterfaz {
    void delete();
    void create();
    void update();
    void read();
    void muestraEstado(String tipoHabitacion, Date desdeFecha, Date hastaFecha);
    void abrirArchivoCsvHabitaciones(Date desdeFecha, Date hastaFecha);
    void muestraEstado(Date desdeFecha, Date hastaFecha);
}
