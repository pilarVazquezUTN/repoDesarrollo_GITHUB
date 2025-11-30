package com.hotelPremier.classes.habitacion;

import java.util.List;

import com.hotelPremier.classes.estadia.Estadia;
import com.hotelPremier.classes.reserva.Reserva;

public class HabitacionEstadosDTO {

    private Habitacion habitacion;
    private List<Reserva> reservas;
    private List<Estadia> estadias;

    public HabitacionEstadosDTO(Habitacion habitacion, 
                                List<Reserva> reservas, 
                                List<Estadia> estadias) {
        this.habitacion = habitacion;
        this.reservas = reservas;
        this.estadias = estadias;
    }

}
