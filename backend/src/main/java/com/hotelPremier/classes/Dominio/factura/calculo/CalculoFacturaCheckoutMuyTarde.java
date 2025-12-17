package com.hotelPremier.classes.Dominio.factura.calculo;

import com.hotelPremier.classes.Dominio.Estadia;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Estrategia de cálculo para facturas con checkout muy tarde (después de las 18:00).
 * Aplica recargo de un día completo adicional.
 */
public class CalculoFacturaCheckoutMuyTarde extends CalculoFacturaBase {

    private static final int HORA_LIMITE = 18;

    @Override
    protected BigDecimal calcularRecargoCheckout(Estadia estadia, DatosFactura datos) {
        int horaCheckout = obtenerHoraCheckout(estadia, datos);
        
        // Check-out después de las 18:00 → se cobra un día completo adicional
        if (horaCheckout > HORA_LIMITE) {
            return obtenerPrecioDiario(estadia).setScale(2, RoundingMode.HALF_UP);
        }
        
        return BigDecimal.ZERO;
    }
}

