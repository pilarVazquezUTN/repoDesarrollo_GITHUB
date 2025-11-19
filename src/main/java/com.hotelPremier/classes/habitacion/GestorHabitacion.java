package classes.habitacion;

import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import classes.DAOFactory;

public class GestorHabitacion {
    private HabitacionDAO habitacionDAO = (HabitacionDAO) DAOFactory.create(DAOFactory.HABITACION);
    private String idEmpleado;

    /**
     * id del gestor
     * @param idEmpleado
     */
    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    /**
     * get del id gestor
     * @return
     */
    public String getIdEmpleado() {
        return this.idEmpleado;
    }

    /**
     * estado habitaciones
     * @param tipoHabitacion
     * @param desdeFecha
     * @param hastaFecha
     */
    public void muestraEstado(String tipoHabitacion, Date desdeFecha, Date hastaFecha){
        habitacionDAO.muestraEstado(tipoHabitacion,desdeFecha,hastaFecha);
    }

    /**
     * modificar estado habitaciones
     */
    void modificarEstado(){

    }
}
