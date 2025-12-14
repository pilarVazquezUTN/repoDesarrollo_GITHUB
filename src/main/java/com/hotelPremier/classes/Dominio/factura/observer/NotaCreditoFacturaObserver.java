package com.hotelPremier.classes.Dominio.factura.observer;

import com.hotelPremier.classes.Dominio.Factura;
import com.hotelPremier.classes.Dominio.NotaDeCredito;

/**
 * Observer que reacciona cuando una factura pasa a estado CANCELADA.
 * 
 * Consecuencias del dominio:
 * - Aplica la nota de crédito
 * - El total queda en 0
 * 
 * Nota: El total ya se establece en 0 por el patrón State (en FacturaPendiente.aplicarNotaCredito()).
 * Este observer valida que la nota de crédito esté asociada.
 */
public class NotaCreditoFacturaObserver implements FacturaObserver {

    @Override
    public void actualizar(Factura factura) {
        // Este observer se registra exclusivamente cuando la factura pasa a CANCELADA.
        // No es necesario validar el estado: confiamos en que el observer se registra
        // solo en el contexto correcto (aplicarNotaCredito()).

        // Verificar que la nota de crédito esté asociada
        NotaDeCredito notaCredito = factura.getNotaDeCredito();
        if (notaCredito == null) {
            // La nota de crédito debe estar asociada antes de cancelar
            // Si no está, es un error de flujo
            throw new IllegalStateException("No se puede cancelar la factura sin una nota de crédito asociada");
        }

        // Verificar que el total esté en 0 (ya establecido por State)
        if (factura.getTotal() != 0) {
            throw new IllegalStateException("El total de la factura cancelada debe ser 0");
        }

        // La nota de crédito está aplicada y el total es 0
        // No se requiere acción adicional, solo validación
    }
}

