package com.hotelPremier.classes.Dominio.medioDePago.strategy;

import com.hotelPremier.classes.Dominio.Pago;
import com.hotelPremier.classes.Dominio.medioDePago.MedioDePago;
import java.math.BigDecimal;

/**
 * Estrategia para procesamiento de pagos en efectivo.
 * No aplica recargos ni intereses. El importe final es igual al monto base.
 */
public class PagoEfectivoStrategy implements MedioPagoStrategy {

    @Override
    public void validar(MedioDePago medioPago, Pago pago) {
        // Efectivo no requiere validaciones especiales
        if (medioPago.getMonto() <= 0) {
            throw new IllegalArgumentException("El monto del pago en efectivo debe ser mayor a cero");
        }
    }

    @Override
    public BigDecimal calcularImporteFinal(BigDecimal montoBase, MedioDePago medioPago) {
        // Efectivo: sin recargos ni intereses
        return montoBase;
    }
}

