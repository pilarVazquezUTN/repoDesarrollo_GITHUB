package com.hotelPremier.classes.Dominio.medioDePago.strategy;

import com.hotelPremier.classes.Dominio.Pago;
import com.hotelPremier.classes.Dominio.medioDePago.MedioDePago;
import com.hotelPremier.classes.Dominio.medioDePago.MonedaExtranjera;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Estrategia para procesamiento de pagos en moneda extranjera.
 * Debe convertir el monto a moneda local usando una cotización.
 * Puede aplicar un recargo configurable.
 */
public class PagoMonedaExtranjeraStrategy implements MedioPagoStrategy {

    // Cotizaciones por tipo de moneda (en un sistema real, esto vendría de un servicio externo)
    private static final BigDecimal COTIZACION_USD = new BigDecimal("1000.0"); // Ejemplo: 1 USD = 1000 pesos
    private static final BigDecimal COTIZACION_EUR = new BigDecimal("1100.0"); // Ejemplo: 1 EUR = 1100 pesos
    private static final BigDecimal RECARGO_MONEDA_EXTRANJERA = new BigDecimal("0.05"); // 5% de recargo

    @Override
    public void validar(MedioDePago medioPago, Pago pago) {
        if (!(medioPago instanceof MonedaExtranjera monedaExtranjera)) {
            throw new IllegalArgumentException("El medio de pago no es moneda extranjera");
        }

        if (monedaExtranjera.getTipoMoneda() == null || monedaExtranjera.getTipoMoneda().trim().isEmpty()) {
            throw new IllegalArgumentException("La moneda extranjera debe tener un tipo de moneda especificado");
        }

        // Validar que el tipo de moneda sea soportado
        String tipoMoneda = monedaExtranjera.getTipoMoneda().toUpperCase();
        if (!tipoMoneda.equals("USD") && !tipoMoneda.equals("EUR") && !tipoMoneda.equals("BRL")) {
            throw new IllegalArgumentException("Tipo de moneda no soportado: " + tipoMoneda + ". Soportadas: USD, EUR, BRL");
        }

        if (medioPago.getMonto() <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser mayor a cero");
        }

        if (medioPago.getFecha() == null) {
            throw new IllegalArgumentException("La fecha del pago es obligatoria");
        }
    }

    @Override
    public BigDecimal calcularImporteFinal(BigDecimal montoBase, MedioDePago medioPago) {
        if (!(medioPago instanceof MonedaExtranjera monedaExtranjera)) {
            return montoBase;
        }

        // Obtener cotización según tipo de moneda
        BigDecimal cotizacion = obtenerCotizacion(monedaExtranjera.getTipoMoneda());

        // Convertir a moneda local
        BigDecimal montoEnMonedaLocal = montoBase.multiply(cotizacion);

        // Aplicar recargo configurable
        BigDecimal recargo = montoEnMonedaLocal.multiply(RECARGO_MONEDA_EXTRANJERA);

        return montoEnMonedaLocal.add(recargo).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Obtiene la cotización para un tipo de moneda.
     * En un sistema real, esto vendría de un servicio externo o base de datos.
     */
    private BigDecimal obtenerCotizacion(String tipoMoneda) {
        return switch (tipoMoneda.toUpperCase()) {
            case "USD" -> COTIZACION_USD;
            case "EUR" -> COTIZACION_EUR;
            case "BRL" -> new BigDecimal("200.0"); // Ejemplo
            default -> throw new IllegalArgumentException("Tipo de moneda no soportado: " + tipoMoneda);
        };
    }
}

