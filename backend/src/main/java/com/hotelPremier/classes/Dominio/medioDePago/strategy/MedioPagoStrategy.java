package com.hotelPremier.classes.Dominio.medioDePago.strategy;

import com.hotelPremier.classes.Dominio.Pago;
import com.hotelPremier.classes.Dominio.medioDePago.MedioDePago;
import java.math.BigDecimal;

/**
 * Interfaz que define la estrategia para procesar un medio de pago.
 * Implementa el patrón Strategy para permitir diferentes comportamientos según el medio.
 */
public interface MedioPagoStrategy {

    /**
     * Valida los datos del medio de pago.
     * 
     * @param medioPago El medio de pago a validar
     * @param pago El pago asociado
     * @throws IllegalArgumentException si la validación falla
     */
    void validar(MedioDePago medioPago, Pago pago);

    /**
     * Calcula el importe final del pago aplicando recargos, intereses o conversiones.
     * 
     * @param montoBase El monto base del pago
     * @param medioPago El medio de pago utilizado
     * @return El importe final calculado
     */
    BigDecimal calcularImporteFinal(BigDecimal montoBase, MedioDePago medioPago);
}

