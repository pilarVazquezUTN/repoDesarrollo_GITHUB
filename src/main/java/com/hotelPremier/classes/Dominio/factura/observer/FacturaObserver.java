package com.hotelPremier.classes.Dominio.factura.observer;

import com.hotelPremier.classes.Dominio.Factura;

/**
 * Interfaz que define el contrato para los observadores de Factura.
 * Implementa el patrón Observer para reaccionar a cambios en el estado de la factura.
 */
public interface FacturaObserver {

    /**
     * Método llamado cuando la factura cambia de estado.
     * Los observadores concretos implementan la lógica de reacción.
     * 
     * @param factura La factura que cambió de estado
     */
    void actualizar(Factura factura);
}

