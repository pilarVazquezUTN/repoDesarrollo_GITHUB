package com.hotelPremier.classes.Dominio.factura.estado;

import com.hotelPremier.classes.Dominio.Factura;
import com.hotelPremier.exception.NegocioException;

/**
 * Clase abstracta que define el comportamiento según el estado de la factura.
 * Implementa el patrón State para manejar las transiciones de estado de manera controlada.
 */
public abstract class EstadoFactura {

    /**
     * Retorna el nombre del estado (PENDIENTE, PAGADA, CANCELADA)
     */
    public abstract String getNombre();

    /**
     * Intenta pagar la factura. Por defecto lanza excepción si no es posible.
     */
    public void pagar(Factura factura) {
        throw new NegocioException("No se puede pagar en estado " + getNombre());
    }

    /**
     * Intenta cancelar la factura. Por defecto lanza excepción si no es posible.
     */
    public void cancelar(Factura factura) {
        throw new NegocioException("No se puede cancelar en estado " + getNombre());
    }

    /**
     * Intenta aplicar una nota de crédito a la factura. Por defecto lanza excepción si no es posible.
     */
    public void aplicarNotaCredito(Factura factura) {
        throw new NegocioException("No se puede aplicar nota de crédito en estado " + getNombre());
    }
}

