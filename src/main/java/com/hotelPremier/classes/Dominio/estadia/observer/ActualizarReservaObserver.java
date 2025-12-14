package com.hotelPremier.classes.Dominio.estadia.observer;

import com.hotelPremier.classes.Dominio.Estadia;
import com.hotelPremier.classes.Dominio.Reserva;

/**
 * Observer que actualiza el estado de la reserva cuando una estadía pasa a ENCURSO.
 * Cambia el estado de la reserva a CONSUMIDA (CHECKIN/CUMPLIDA).
 */
public class ActualizarReservaObserver implements EstadiaObserver {

    @Override
    public void actualizar(Estadia estadia) {
        // Este observer se registra exclusivamente cuando la estadía pasa a ENCURSO.
        // No es necesario validar el estado de la estadía: confiamos en que el observer
        // se registra solo en el contexto correcto (iniciarEstadia()).
        
        Reserva reserva = estadia.getReserva();
        if (reserva != null && "PENDIENTE".equals(reserva.getEstado())) {
            // Cambiar la reserva a CONSUMIDA usando el método centralizado del State
            // No usar checkIn() porque ya se creó la estadía, solo cambiar el estado
            reserva.setEstadoReserva(new com.hotelPremier.classes.Dominio.reserva.estado.ReservaConsumida());
        }
    }
}

