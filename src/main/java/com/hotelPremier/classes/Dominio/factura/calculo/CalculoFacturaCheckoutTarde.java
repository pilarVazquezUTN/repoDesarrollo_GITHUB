package com.hotelPremier.classes.Dominio.factura.calculo;

import com.hotelPremier.classes.Dominio.Estadia;
import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * Estrategia de cálculo para facturas con checkout tarde (entre 11:01 y 18:00).
 * Aplica recargo del 50% del valor diario.
 */
public class CalculoFacturaCheckoutTarde extends CalculoFacturaBase {

    private static final int HORA_LIMITE_INFERIOR = 11;
    private static final int HORA_LIMITE_SUPERIOR = 18;

    @Override
    protected BigDecimal calcularRecargoCheckout(Estadia estadia, DatosFactura datos) {
        int horaCheckout = obtenerHoraCheckout(estadia, datos);
        
        // Check-out entre 11:01 y 18:00 → recargo del 50% del valor diario
        if (horaCheckout > HORA_LIMITE_INFERIOR && horaCheckout <= HORA_LIMITE_SUPERIOR) {
            BigDecimal precioDiario = obtenerPrecioDiario(estadia);
            return precioDiario.multiply(new BigDecimal("0.50"))
                    .setScale(2, RoundingMode.HALF_UP);
        }
        
        return BigDecimal.ZERO;
    }
}

