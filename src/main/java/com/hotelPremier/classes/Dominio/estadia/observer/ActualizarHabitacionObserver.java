package com.hotelPremier.classes.Dominio.estadia.observer;

import com.hotelPremier.classes.Dominio.Estadia;
import com.hotelPremier.classes.Dominio.Habitacion;

/**
 * Observer que actualiza el estado de la habitación cuando una estadía pasa a ENCURSO.
 * Cambia el estado de la habitación a OCUPADA.
 */
public class ActualizarHabitacionObserver implements EstadiaObserver {

    @Override
    public void actualizar(Estadia estadia) {
        // Este observer se registra exclusivamente cuando la estadía pasa a ENCURSO.
        // No es necesario validar el estado: confiamos en que el observer se registra
        // solo en el contexto correcto (iniciarEstadia()).
        
        Habitacion habitacion = estadia.getHabitacion();
        if (habitacion != null) {
            habitacion.setEstado("OCUPADA");
        }
    }
}

