package com.hotelPremier.classes.Dominio.medioDePago.strategy;

import com.hotelPremier.classes.Dominio.Pago;
import com.hotelPremier.classes.Dominio.medioDePago.MedioDePago;
import com.hotelPremier.classes.Dominio.medioDePago.MonedaExtranjera;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Estrategia para procesamiento de pagos en moneda extranjera.
 * Debe convertir el monto a moneda local usando una cotización proporcionada.
 * Puede aplicar un recargo configurable.
 */
public class PagoMonedaExtranjeraStrategy implements MedioPagoStrategy {

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

        // Validar que se proporcione la cotización
        if (monedaExtranjera.getCotizacion() == null || monedaExtranjera.getCotizacion() <= 0) {
            throw new IllegalArgumentException("La cotización es obligatoria y debe ser mayor a cero");
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

        // Obtener cotización proporcionada
        BigDecimal cotizacion = BigDecimal.valueOf(monedaExtranjera.getCotizacion());

        // Convertir el monto de moneda extranjera a moneda local (ARS)
        BigDecimal montoEnMonedaLocal = montoBase.multiply(cotizacion);

        // Aplicar recargo configurable
        BigDecimal recargo = montoEnMonedaLocal.multiply(RECARGO_MONEDA_EXTRANJERA);

        return montoEnMonedaLocal.add(recargo).setScale(2, RoundingMode.HALF_UP);
    }
}

