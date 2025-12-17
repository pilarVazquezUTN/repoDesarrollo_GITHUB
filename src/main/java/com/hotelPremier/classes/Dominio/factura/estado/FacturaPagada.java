package com.hotelPremier.classes.Dominio.factura.estado;

import com.hotelPremier.classes.Dominio.Factura;

/**
 * Estado PAGADA de una factura.
 * Permite: aplicar nota de crédito (que cancela)
 * No permite: pagar nuevamente, cancelar directamente
 */
public class FacturaPagada extends EstadoFactura {

    @Override
    public String getNombre() {
        return "PAGADA";
    }

    @Override
    public void aplicarNotaCredito(Factura factura) {
        // Establecer el total a 0 ANTES de cambiar el estado
        // para que los observers vean el total correcto
        factura.setTotal(0);
        factura.setEstadoFactura(new FacturaCancelada());
    }
}

