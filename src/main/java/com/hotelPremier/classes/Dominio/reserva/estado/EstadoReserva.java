package com.hotelPremier.classes.Dominio.reserva.estado;

import com.hotelPremier.classes.Dominio.Reserva;
import com.hotelPremier.classes.Dominio.Estadia;

/**
 * Clase abstracta que define el comportamiento según el estado de la reserva.
 * Implementa el patrón State para manejar las transiciones de estado de manera controlada.
 */
public abstract class EstadoReserva {

    /**
     * Retorna el nombre del estado (PENDIENTE, CONSUMIDA, CANCELADA)
     */
    public abstract String getNombre();

    /**
     * Intenta cancelar la reserva. Por defecto lanza excepción si no es posible.
     */
    public void cancelar(Reserva reserva) {
        throw new IllegalStateException("No se puede cancelar en estado " + getNombre());
    }

    /**
     * Intenta hacer check-in de la reserva (consumirla y crear estadía).
     * Por defecto lanza excepción si no es posible.
     * 
     * @param reserva La reserva que se está consumiendo
     * @return La nueva Estadia creada en estado ENCURSO
     */
    public Estadia checkIn(Reserva reserva) {
        throw new IllegalStateException("No se puede hacer check-in en estado " + getNombre());
    }
}

