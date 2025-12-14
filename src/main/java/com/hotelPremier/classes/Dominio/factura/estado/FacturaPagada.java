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
        factura.setEstadoFactura(new FacturaCancelada());
        factura.setTotal(0);
    }
}

