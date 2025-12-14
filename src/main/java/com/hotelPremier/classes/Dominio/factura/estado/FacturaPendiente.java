package com.hotelPremier.classes.Dominio.factura.estado;

import com.hotelPremier.classes.Dominio.Factura;

/**
 * Estado PENDIENTE de una factura.
 * Permite: pagar, aplicar nota de crédito (que cancela)
 * No permite: cancelar directamente
 */
public class FacturaPendiente extends EstadoFactura {

    @Override
    public String getNombre() {
        return "PENDIENTE";
    }

    @Override
    public void pagar(Factura factura) {
        factura.setEstadoFactura(new FacturaPagada());
    }

    @Override
    public void aplicarNotaCredito(Factura factura) {
        factura.setEstadoFactura(new FacturaCancelada());
        factura.setTotal(0);
    }
}

