package com.hotelPremier.classes.Dominio.factura.observer;

import com.hotelPremier.classes.Dominio.Factura;
import com.hotelPremier.classes.Dominio.Estadia;
import com.hotelPremier.classes.Dominio.Habitacion;

/**
 * Observer que reacciona cuando una factura pasa a estado GENERADA.
 * 
 * Consecuencias del dominio:
 * - Se realiza el CHECKOUT del huésped
 * - La estadía debe finalizar
 * - La habitación debe liberarse
 */
public class CheckoutFacturaObserver implements FacturaObserver {

    @Override
    public void actualizar(Factura factura) {
        // Este observer se registra exclusivamente cuando la factura pasa a GENERADA.
        // No es necesario validar el estado: confiamos en que el observer se registra
        // solo en el contexto correcto (generarFacturaFinal()).
        
        Estadia estadia = factura.getEstadia();
        if (estadia != null) {
            // Finalizar la estadía
            estadia.finalizar();
            
            // Liberar la habitación
            Habitacion habitacion = estadia.getHabitacion();
            if (habitacion != null) {
                habitacion.setEstado("DISPONIBLE");
            }
        }
    }
}

