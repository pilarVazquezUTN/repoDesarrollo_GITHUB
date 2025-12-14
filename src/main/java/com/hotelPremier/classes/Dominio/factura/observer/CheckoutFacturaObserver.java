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
        // Solo reaccionar cuando la factura pasa a GENERADA
        if (!"GENERADA".equals(factura.getEstado())) {
            return;
        }

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

