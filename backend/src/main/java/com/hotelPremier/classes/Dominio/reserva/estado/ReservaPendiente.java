package com.hotelPremier.classes.Dominio.reserva.estado;

import com.hotelPremier.classes.Dominio.Reserva;
import com.hotelPremier.classes.Dominio.Estadia;

/**
 * Estado PENDIENTE de una reserva.
 * Representa una intención de ocupación futura.
 * 
 * Permite:
 * - cancelar la reserva
 * - hacer check-in (consumir la reserva y crear estadía)
 * 
 * No permite:
 * - ninguna otra operación
 */
public class ReservaPendiente extends EstadoReserva {

    @Override
    public String getNombre() {
        return "PENDIENTE";
    }

    @Override
    public void cancelar(Reserva reserva) {
        reserva.setEstadoReserva(new ReservaCancelada());
    }

    @Override
    public Estadia checkIn(Reserva reserva) {
        // 1. Cambiar estado de la reserva a CONSUMIDA
        reserva.setEstadoReserva(new ReservaConsumida());
        
        // 2. Crear nueva Estadia en estado ENCURSO
        Estadia estadia = new Estadia();
        estadia.setCheckin(reserva.getFecha_desde());
        estadia.setCheckout(reserva.getFecha_hasta());
        estadia.setHabitacion(reserva.getHabitacion());
        estadia.setReserva(reserva);
        // El estado ENCURSO se inicializa automáticamente en el constructor de Estadia
        
        // 3. Asociar la estadía con la reserva
        reserva.setEstadia(estadia);
        
        return estadia;
    }

    @Override
    public void consumir(Reserva reserva) {
        // Consumir la reserva cambiando su estado a CONSUMIDA
        // Sin crear una nueva estadía (útil cuando la estadía ya existe)
        reserva.setEstadoReserva(new ReservaConsumida());
    }
}

