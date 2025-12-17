package com.hotelPremier.classes.Dominio.estadia.estado;

/**
 * Estado FINALIZADA de una estadía.
 * El huésped hizo checkout.
 * 
 * Permite:
 * - solo consultas
 * - ver facturas
 * 
 * No permite:
 * - agregar servicios
 * - agregar huéspedes
 * - facturar
 * - modificar fechas
 * 
 * Estado terminal natural.
 */
public class EstadiaFinalizada extends EstadoEstadia {

    @Override
    public String getNombre() {
        return "FINALIZADA";
    }
    
    // Todos los métodos heredan el comportamiento por defecto que lanza excepción
    // No se puede hacer ninguna operación en este estado
}

