package com.hotelPremier.classes.Dominio.medioDePago.strategy;

import com.hotelPremier.classes.Dominio.medioDePago.MedioDePago;
import com.hotelPremier.classes.Dominio.medioDePago.Cheque;
import com.hotelPremier.classes.Dominio.medioDePago.TarjetaCredito;
import com.hotelPremier.classes.Dominio.medioDePago.TarjetaDebito;
import com.hotelPremier.classes.Dominio.medioDePago.MonedaExtranjera;
import com.hotelPremier.classes.Dominio.medioDePago.MonedaLocal;

/**
 * Clase que selecciona la estrategia de procesamiento apropiada según el tipo de medio de pago.
 * La selección se basa en la clase concreta del medio de pago.
 */
public class SelectorMedioPagoStrategy {

    /**
     * Selecciona la estrategia de procesamiento según el tipo de medio de pago.
     * 
     * @param medioPago El medio de pago para el cual se necesita la estrategia
     * @return La estrategia de procesamiento correspondiente
     * @throws IllegalArgumentException si el tipo de medio de pago no es reconocido
     */
    public static MedioPagoStrategy seleccionarEstrategia(MedioDePago medioPago) {
        if (medioPago == null) {
            throw new IllegalArgumentException("El medio de pago no puede ser nulo");
        }

        // Selección basada en la clase concreta (polimorfismo)
        if (medioPago instanceof TarjetaCredito) {
            return new PagoTarjetaCreditoStrategy();
        }
        
        if (medioPago instanceof TarjetaDebito) {
            return new PagoTarjetaDebitoStrategy();
        }
        
        if (medioPago instanceof Cheque) {
            return new PagoChequeStrategy();
        }
        
        if (medioPago instanceof MonedaExtranjera) {
            return new PagoMonedaExtranjeraStrategy();
        }
        
        if (medioPago instanceof MonedaLocal) {
            // MonedaLocal puede representar efectivo o moneda local
            // Por defecto, se trata como efectivo (sin recargos)
            return new PagoEfectivoStrategy();
        }

        // Si no se reconoce el tipo, usar estrategia de efectivo por defecto
        return new PagoEfectivoStrategy();
    }

    /**
     * Selecciona la estrategia según el tipo de medio de pago (String).
     * Útil cuando se recibe el tipo desde un DTO antes de crear la entidad.
     * 
     * @param tipoMedioPago El tipo de medio de pago como String
     * @return La estrategia de procesamiento correspondiente
     */
    public static MedioPagoStrategy seleccionarEstrategiaPorTipo(String tipoMedioPago) {
        if (tipoMedioPago == null || tipoMedioPago.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de medio de pago no puede ser nulo o vacío");
        }

        return switch (tipoMedioPago.toUpperCase()) {
            case "TARJETA_CREDITO" -> new PagoTarjetaCreditoStrategy();
            case "TARJETA_DEBITO" -> new PagoTarjetaDebitoStrategy();
            case "CHEQUE" -> new PagoChequeStrategy();
            case "MONEDA_EXTRANJERA" -> new PagoMonedaExtranjeraStrategy();
            case "MONEDA_LOCAL", "EFECTIVO" -> new PagoEfectivoStrategy();
            default -> throw new IllegalArgumentException("Tipo de medio de pago no reconocido: " + tipoMedioPago);
        };
    }
}

