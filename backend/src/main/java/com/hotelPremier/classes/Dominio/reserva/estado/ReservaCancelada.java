package com.hotelPremier.classes.Dominio.reserva.estado;

/**
 * Estado CANCELADA de una reserva.
 * La reserva fue cancelada y ya no puede ser utilizada.
 * 
 * No permite ninguna operación adicional.
 * Hereda todos los métodos bloqueados de EstadoReserva.
 */
public class ReservaCancelada extends EstadoReserva {

    @Override
    public String getNombre() {
        return "CANCELADA";
    }
    
    // Todos los métodos heredan el comportamiento por defecto que lanza excepción
    // No se puede hacer ninguna operación en este estado
}

