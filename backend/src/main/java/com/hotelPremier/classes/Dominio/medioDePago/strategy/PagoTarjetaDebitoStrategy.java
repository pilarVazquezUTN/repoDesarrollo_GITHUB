package com.hotelPremier.classes.Dominio.medioDePago.strategy;

import com.hotelPremier.classes.Dominio.Pago;
import com.hotelPremier.classes.Dominio.medioDePago.MedioDePago;
import com.hotelPremier.classes.Dominio.medioDePago.TarjetaDebito;
import java.math.BigDecimal;

/**
 * Estrategia para procesamiento de pagos con tarjeta de débito.
 * No aplica intereses. Valida número de tarjeta y vencimiento.
 */
public class PagoTarjetaDebitoStrategy implements MedioPagoStrategy {

    @Override
    public void validar(MedioDePago medioPago, Pago pago) {
        if (!(medioPago instanceof TarjetaDebito tarjeta)) {
            throw new IllegalArgumentException("El medio de pago no es una tarjeta de débito");
        }

        if (tarjeta.getBanco() == null || tarjeta.getBanco().trim().isEmpty()) {
            throw new IllegalArgumentException("La tarjeta de débito debe tener un banco asociado");
        }

        if (tarjeta.getDniTitular() == null || tarjeta.getDniTitular().trim().isEmpty()) {
            throw new IllegalArgumentException("La tarjeta de débito debe tener el DNI del titular");
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
        // Tarjeta de débito: sin intereses ni recargos
        return montoBase;
    }
}

