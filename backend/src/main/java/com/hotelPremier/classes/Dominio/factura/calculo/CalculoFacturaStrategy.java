package com.hotelPremier.classes.Dominio.factura.calculo;

import com.hotelPremier.classes.Dominio.Estadia;
import java.math.BigDecimal;

/**
 * Interfaz que define la estrategia para calcular el total de una factura.
 * Implementa el patrón Strategy para permitir diferentes algoritmos de cálculo.
 */
public interface CalculoFacturaStrategy {

    /**
     * Calcula el total de la factura según la estrategia implementada.
     * 
     * @param estadia La estadía para la cual se calcula la factura
     * @param datos Los datos adicionales necesarios para el cálculo
     * @return El total calculado como BigDecimal para precisión
     */
    BigDecimal calcularTotal(Estadia estadia, DatosFactura datos);
}

