package com.hotelPremier.classes.Dominio.factura.estado;

import com.hotelPremier.classes.Dominio.Factura;

/**
 * Estado GENERADA de una factura.
 * Representa una factura que ha sido generada (factura final).
 * Permite: pagar, aplicar nota de crédito (que cancela)
 * No permite: cancelar directamente
 */
public class FacturaGenerada extends EstadoFactura {

    @Override
    public String getNombre() {
        return "GENERADA";
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

