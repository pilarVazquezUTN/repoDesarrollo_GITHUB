package com.hotelPremier.classes.Dominio.factura.observer;

import com.hotelPremier.classes.Dominio.Factura;
import com.hotelPremier.classes.Dominio.Pago;

/**
 * Observer que reacciona cuando una factura pasa a estado PAGADA.
 * 
 * Consecuencias del dominio:
 * - Se registra el pago correspondiente
 * 
 * Nota: El pago ya está asociado a la factura antes de cambiar el estado.
 * Este observer solo confirma que el pago está registrado.
 */
public class PagoFacturaObserver implements FacturaObserver {

    @Override
    public void actualizar(Factura factura) {
        // Este observer se registra exclusivamente cuando la factura pasa a PAGADA.
        // No es necesario validar el estado: confiamos en que el observer se registra
        // solo en el contexto correcto (pagar()).

        // Verificar que el pago esté asociado
        Pago pago = factura.getPago();
        if (pago == null) {
            // El pago debe estar registrado antes de cambiar a PAGADA
            // Si no está, es un error de flujo
            throw new IllegalStateException("No se puede marcar la factura como PAGADA sin un pago asociado");
        }

        // El pago ya está registrado y asociado a la factura
        // No se requiere acción adicional, solo validación
    }
}

