package com.hotelPremier.classes.habitacion;

import java.util.Date;

public interface HabitacionDAOInterfaz {
    void delete();
    void create();
    void update();
    void read();

    /**
     *
     * @param tipoHabitacion
     * @param desdeFecha se ingresa
     * @param hastaFecha se ingresa
     */
    void muestraEstado(String tipoHabitacion, Date desdeFecha, Date hastaFecha);

    /**
     * se abre el archivo de las habitaciones
     * @param desdeFecha
     * @param hastaFecha
     */
    void abrirArchivoCsvHabitaciones(Date desdeFecha, Date hastaFecha);

    /**
     *
     * @param desdeFecha
     * @param hastaFecha
     */
    void muestraEstado(Date desdeFecha, Date hastaFecha);
}
