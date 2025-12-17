package com.hotelPremier.classes.DTO;

import java.util.List;

import com.hotelPremier.classes.Dominio.Estadia;
import com.hotelPremier.classes.Dominio.Habitacion;
import com.hotelPremier.classes.Dominio.Reserva;

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

    public Habitacion getHabitacion() {
        return habitacion;
    }
    public void setHabitacion(Habitacion habitacion) {

        this.habitacion = habitacion;
    }
    public List<Reserva> getReservas() {
        return reservas;
    }
    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;

    }
    public List<Estadia> getEstadias() {
        return estadias;
    }
    public void setEstadias(List<Estadia> estadias) {
        this.estadias = estadias;
    }


}
