package com.hotelPremier.classes.Dominio.estadia.observer;

import com.hotelPremier.classes.Dominio.Estadia;

/**
 * Interfaz que define el contrato para los observadores de Estadia.
 * Implementa el patrón Observer para reaccionar a cambios en el estado de la estadía.
 */
public interface EstadiaObserver {

    /**
     * Método llamado cuando la estadía cambia de estado.
     * Los observadores concretos implementan la lógica de reacción.
     * 
     * @param estadia La estadía que cambió de estado
     */
    void actualizar(Estadia estadia);
}

