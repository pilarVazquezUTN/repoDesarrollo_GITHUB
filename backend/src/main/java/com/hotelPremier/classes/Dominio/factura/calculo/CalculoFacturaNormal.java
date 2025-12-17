package com.hotelPremier.classes.Dominio.factura.calculo;

import com.hotelPremier.classes.Dominio.Estadia;
import java.math.BigDecimal;

/**
 * Estrategia de cálculo para facturas con checkout normal (hasta las 11:00).
 * No aplica recargo por horario de checkout.
 */
public class CalculoFacturaNormal extends CalculoFacturaBase {

    @Override
    protected BigDecimal calcularRecargoCheckout(Estadia estadia, DatosFactura datos) {
        // Checkout hasta las 11:00 → sin recargo
        return BigDecimal.ZERO;
    }
}

