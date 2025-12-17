package com.hotelPremier.classes.Dominio.factura.estado;

/**
 * Estado CANCELADA de una factura.
 * No permite ninguna operación adicional.
 * Hereda todos los métodos bloqueados de EstadoFactura.
 */
public class FacturaCancelada extends EstadoFactura {

    @Override
    public String getNombre() {
        return "CANCELADA";
    }
}

