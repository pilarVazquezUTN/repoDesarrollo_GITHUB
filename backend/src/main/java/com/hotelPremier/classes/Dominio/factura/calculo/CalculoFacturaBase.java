package com.hotelPremier.classes.Dominio.factura.calculo;

import com.hotelPremier.classes.Dominio.Estadia;
import com.hotelPremier.classes.Dominio.Habitacion;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

/**
 * Clase base abstracta que contiene la lógica común para el cálculo de facturas.
 * Las estrategias concretas extienden esta clase y solo implementan la parte específica.
 */
public abstract class CalculoFacturaBase implements CalculoFacturaStrategy {

    // Constantes para el cálculo
    private static final BigDecimal IVA = new BigDecimal("0.21"); // 21% IVA
    protected static final int HORA_LIMITE_CHECKOUT_NORMAL = 11; // 11:00

    /**
     * Calcula el costo base de la estadía (precio por noche × cantidad de noches).
     */
    protected BigDecimal calcularCostoBase(Estadia estadia) {
        if (estadia.getHabitacion() == null) {
            throw new IllegalArgumentException("La estadía debe tener una habitación asociada");
        }

        Habitacion habitacion = estadia.getHabitacion();
        float precioPorNoche = habitacion.getPrecio();
        
        long noches = calcularCantidadNoches(estadia);
        
        return BigDecimal.valueOf(precioPorNoche)
                .multiply(BigDecimal.valueOf(noches))
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula la cantidad de noches entre check-in y check-out.
     * Si check-in y check-out son el mismo día, se considera mínimo 1 noche.
     */
    protected long calcularCantidadNoches(Estadia estadia) {
        if (estadia.getCheckin() == null || estadia.getCheckout() == null) {
            throw new IllegalArgumentException("La estadía debe tener fechas de check-in y check-out");
        }

        long diffInMillis = estadia.getCheckout().getTime() - estadia.getCheckin().getTime();
        long noches = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
        
        // Mínimo 1 noche: si check-in y check-out son el mismo día, se cobra 1 noche
        return Math.max(1, noches);
    }

    /**
     * Calcula el descuento por cantidad de noches según el tipo de habitación.
     * Por defecto retorna 0, puede ser sobrescrito por implementaciones específicas.
     */
    protected BigDecimal calcularDescuentoPorNoches(Estadia estadia, BigDecimal costoBase) {
        long noches = calcularCantidadNoches(estadia);
        
        // Regla: Si supera 7 noches, descuento del 10%
        // Si supera 14 noches, descuento del 15%
        // Esto es configurable según tipo de habitación
        if (noches > 14) {
            return costoBase.multiply(new BigDecimal("0.15")).setScale(2, RoundingMode.HALF_UP);
        } else if (noches > 7) {
            return costoBase.multiply(new BigDecimal("0.10")).setScale(2, RoundingMode.HALF_UP);
        }
        
        return BigDecimal.ZERO;
    }

    /**
     * Calcula el total de los consumos/servicios extra seleccionados.
     */
    protected BigDecimal calcularTotalConsumos(DatosFactura datos) {
        if (datos.getConsumosSeleccionados() == null || datos.getConsumosSeleccionados().isEmpty()) {
            return BigDecimal.ZERO;
        }

        return datos.getConsumosSeleccionados().stream()
                .filter(consumo -> consumo.getPrecio() != null)
                .map(consumo -> BigDecimal.valueOf(consumo.getPrecio()))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula el IVA según el tipo de factura.
     * Factura A: discrimina IVA (subtotal + IVA)
     * Factura B: IVA incluido (no se discrimina)
     */
    protected BigDecimal calcularIVA(BigDecimal subtotal, String tipoFactura) {
        if (tipoFactura == null || !tipoFactura.equalsIgnoreCase("A")) {
            // Factura B: IVA incluido, no se discrimina
            return BigDecimal.ZERO;
        }
        
        // Factura A: discrimina IVA
        return subtotal.multiply(IVA).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula el recargo por horario de checkout.
     * Debe ser implementado por las clases concretas.
     */
    protected abstract BigDecimal calcularRecargoCheckout(Estadia estadia, DatosFactura datos);

    /**
     * Obtiene la hora del checkout real.
     */
    protected int obtenerHoraCheckout(Estadia estadia, DatosFactura datos) {
        if (datos.getFechaHoraCheckoutReal() == null) {
            // Si no se proporciona, usar la fecha de checkout de la estadía
            return HORA_LIMITE_CHECKOUT_NORMAL; // Por defecto, sin recargo
        }
        return datos.getFechaHoraCheckoutReal().getHour();
    }

    /**
     * Obtiene el precio diario de la habitación.
     */
    protected BigDecimal obtenerPrecioDiario(Estadia estadia) {
        if (estadia.getHabitacion() == null) {
            throw new IllegalArgumentException("La estadía debe tener una habitación asociada");
        }
        return BigDecimal.valueOf(estadia.getHabitacion().getPrecio());
    }

    @Override
    public BigDecimal calcularTotal(Estadia estadia, DatosFactura datos) {
        
        // 1. Costo base (precio × noches)
        BigDecimal costoBase = calcularCostoBase(estadia);
        
        // 2. Descuento por cantidad de noches
        BigDecimal descuento = calcularDescuentoPorNoches(estadia, costoBase);
        BigDecimal subtotalHabitacion = costoBase.subtract(descuento);
        
        // 3. Recargo por horario de checkout (implementado por subclases)
        BigDecimal recargoCheckout = calcularRecargoCheckout(estadia, datos);
        
        // 4. Consumos/servicios extra
        BigDecimal totalConsumos = calcularTotalConsumos(datos);
        
        // 5. Subtotal antes de IVA
        BigDecimal subtotal = subtotalHabitacion.add(recargoCheckout).add(totalConsumos);
        
        // 6. IVA según tipo de factura
        BigDecimal iva = calcularIVA(subtotal, datos.getTipoFactura());
        
        // 7. Total final
        return subtotal.add(iva).setScale(2, RoundingMode.HALF_UP);
    }
}

