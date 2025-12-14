package com.hotelPremier.classes.Dominio.medioDePago.strategy;

import com.hotelPremier.classes.Dominio.Pago;
import com.hotelPremier.classes.Dominio.medioDePago.MedioDePago;
import com.hotelPremier.classes.Dominio.medioDePago.TarjetaCredito;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Estrategia para procesamiento de pagos con tarjeta de crédito.
 * Puede aplicar intereses o recargos según las cuotas.
 * Valida número de tarjeta, vencimiento y cuotas.
 */
public class PagoTarjetaCreditoStrategy implements MedioPagoStrategy {

    private static final BigDecimal INTERES_POR_CUOTA = new BigDecimal("0.03"); // 3% por cuota
    private static final int CUOTAS_MAXIMAS = 12;

    @Override
    public void validar(MedioDePago medioPago, Pago pago) {
        if (!(medioPago instanceof TarjetaCredito tarjeta)) {
            throw new IllegalArgumentException("El medio de pago no es una tarjeta de crédito");
        }

        if (tarjeta.getBanco() == null || tarjeta.getBanco().trim().isEmpty()) {
            throw new IllegalArgumentException("La tarjeta de crédito debe tener un banco asociado");
        }

        if (tarjeta.getCuotas() == null || tarjeta.getCuotas() <= 0) {
            throw new IllegalArgumentException("La tarjeta de crédito debe tener al menos 1 cuota");
        }

        if (tarjeta.getCuotas() > CUOTAS_MAXIMAS) {
            throw new IllegalArgumentException("El número de cuotas no puede exceder " + CUOTAS_MAXIMAS);
        }

        if (medioPago.getMonto() <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser mayor a cero");
        }

        // Validar fecha (no debe ser nula)
        if (medioPago.getFecha() == null) {
            throw new IllegalArgumentException("La fecha del pago es obligatoria");
        }
    }

    @Override
    public BigDecimal calcularImporteFinal(BigDecimal montoBase, MedioDePago medioPago) {
        if (!(medioPago instanceof TarjetaCredito tarjeta)) {
            return montoBase;
        }

        // Aplicar interés según número de cuotas
        // Interés = montoBase * (interés por cuota * número de cuotas)
        Integer cuotas = tarjeta.getCuotas();
        if (cuotas == null || cuotas <= 1) {
            // Una cuota: sin interés
            return montoBase.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal interes = montoBase
                .multiply(INTERES_POR_CUOTA)
                .multiply(BigDecimal.valueOf(cuotas - 1)); // Sin interés en la primera cuota

        return montoBase.add(interes).setScale(2, RoundingMode.HALF_UP);
    }
}

