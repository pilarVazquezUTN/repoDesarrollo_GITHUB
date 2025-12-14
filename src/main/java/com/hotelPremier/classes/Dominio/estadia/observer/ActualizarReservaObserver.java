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
        // Solo reaccionar cuando la estadía pasa a ENCURSO
        if (!"ENCURSO".equals(estadia.getEstado())) {
            return;
        }

        Reserva reserva = estadia.getReserva();
        if (reserva != null && "PENDIENTE".equals(reserva.getEstado())) {
            // Cambiar la reserva a CONSUMIDA (CHECKIN/CUMPLIDA)
            // No usar checkIn() porque ya se creó la estadía, solo cambiar el estado
            reserva.setEstado("CONSUMIDA");
        }
    }
}

