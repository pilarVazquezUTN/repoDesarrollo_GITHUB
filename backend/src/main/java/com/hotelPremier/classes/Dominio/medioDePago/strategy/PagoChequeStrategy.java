package com.hotelPremier.classes.Dominio.medioDePago.strategy;

import com.hotelPremier.classes.Dominio.Pago;
import com.hotelPremier.classes.Dominio.medioDePago.MedioDePago;
import com.hotelPremier.classes.Dominio.medioDePago.Cheque;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Estrategia para procesamiento de pagos con cheque.
 * Debe validar fecha y banco. El importe final no cambia.
 */
public class PagoChequeStrategy implements MedioPagoStrategy {

    @Override
    public void validar(MedioDePago medioPago, Pago pago) {
        if (!(medioPago instanceof Cheque cheque)) {
            throw new IllegalArgumentException("El medio de pago no es un cheque");
        }

        if (cheque.getBanco() == null || cheque.getBanco().trim().isEmpty()) {
            throw new IllegalArgumentException("El cheque debe tener un banco asociado");
        }

        if (cheque.getNumeroCheque() == null || cheque.getNumeroCheque() <= 0) {
            throw new IllegalArgumentException("El cheque debe tener un número válido");
        }

        if (cheque.getPlazo() == null) {
            throw new IllegalArgumentException("El cheque debe tener una fecha de plazo");
        }

        // Validar que el plazo no sea anterior a la fecha actual
        Date fechaActual = new Date();
        if (cheque.getPlazo().before(fechaActual)) {
            throw new IllegalArgumentException("El plazo del cheque no puede ser anterior a la fecha actual");
        }

        if (medioPago.getMonto() <= 0) {
            throw new IllegalArgumentException("El monto del cheque debe ser mayor a cero");
        }
    }

    @Override
    public BigDecimal calcularImporteFinal(BigDecimal montoBase, MedioDePago medioPago) {
        // Cheque: el importe final no cambia
        return montoBase;
    }
}

