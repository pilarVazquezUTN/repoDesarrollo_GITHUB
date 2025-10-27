package Classes.Habitacion;

import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class GestorHabitacion {
    private HabitacionDAO habitacionDAO=new HabitacionDAO();
    private String idEmpleado;

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }
    public String getIdEmpleado() {
        return this.idEmpleado;
    }
    public void muestraEstado(Date desdeFecha, Date hastaFecha){
        habitacionDAO.muestraEstado(desdeFecha,hastaFecha);
    }
    void modificarEstado(){

    }
}
