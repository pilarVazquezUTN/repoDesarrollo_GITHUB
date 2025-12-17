package com.hotelPremier.classes.Dominio.reserva.estado;

/**
 * Estado CONSUMIDA de una reserva.
 * La reserva fue utilizada para crear una estadía (check-in realizado).
 * 
 * No permite ninguna operación adicional.
 * Hereda todos los métodos bloqueados de EstadoReserva.
 */
public class ReservaConsumida extends EstadoReserva {

    @Override
    public String getNombre() {
        return "CONSUMIDA";
    }
    
    // Todos los métodos heredan el comportamiento por defecto que lanza excepción
    // No se puede hacer ninguna operación en este estado
}

