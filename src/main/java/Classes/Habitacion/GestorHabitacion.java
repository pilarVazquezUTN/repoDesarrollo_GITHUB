package Classes.Habitacion;

import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import Classes.DAOFactory;

public class GestorHabitacion {
    private HabitacionDAO habitacionDAO = (HabitacionDAO) DAOFactory.create(DAOFactory.HABITACION);
    private String idEmpleado;

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }
    public String getIdEmpleado() {
        return this.idEmpleado;
    } 
    public void muestraEstado(String tipoHabitacion, Date desdeFecha, Date hastaFecha){
        habitacionDAO.muestraEstado(tipoHabitacion,desdeFecha,hastaFecha);
    }
    void modificarEstado(){

    }
}
