package com.hotelPremier.classes.Dominio.factura.calculo;

import com.hotelPremier.classes.Dominio.Estadia;

/**
 * Clase que selecciona la estrategia de cálculo apropiada según
 * el horario de checkout y otros criterios.
 */
public class SelectorEstrategiaCalculo {

    private static final int HORA_LIMITE_NORMAL = 11;
    private static final int HORA_LIMITE_TARDE = 18;

    /**
     * Selecciona la estrategia de cálculo según el horario de checkout.
     * 
     * @param estadia La estadía para la cual se calcula
     * @param datos Los datos de la factura
     * @return La estrategia de cálculo apropiada
     */
    public static CalculoFacturaStrategy seleccionarEstrategia(Estadia estadia, DatosFactura datos) {
        if (datos.getFechaHoraCheckoutReal() == null) {
            // Si no hay hora de checkout real, usar estrategia normal
            return new CalculoFacturaNormal();
        }

        int horaCheckout = datos.getFechaHoraCheckoutReal().getHour();

        if (horaCheckout <= HORA_LIMITE_NORMAL) {
            // Checkout hasta las 11:00 → sin recargo
            return new CalculoFacturaNormal();
        } else if (horaCheckout <= HORA_LIMITE_TARDE) {
            // Checkout entre 11:01 y 18:00 → recargo del 50%
            return new CalculoFacturaCheckoutTarde();
        } else {
            // Checkout después de las 18:00 → día completo adicional
            return new CalculoFacturaCheckoutMuyTarde();
        }
    }
}

