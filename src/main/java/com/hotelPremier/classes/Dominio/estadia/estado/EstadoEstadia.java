package com.hotelPremier.classes.Dominio.estadia.estado;

import com.hotelPremier.classes.Dominio.Estadia;
import com.hotelPremier.classes.Dominio.Huesped;
import com.hotelPremier.classes.Dominio.servicioExtra.ServicioExtra;
import com.hotelPremier.classes.Dominio.Factura;
import java.util.Date;

/**
 * Clase abstracta que define el comportamiento según el estado de la estadía.
 * Implementa el patrón State para manejar las transiciones de estado de manera controlada.
 */
public abstract class EstadoEstadia {

    /**
     * Retorna el nombre del estado (ENCURSO, FINALIZADA)
     */
    public abstract String getNombre();

    /**
     * Intenta agregar un huésped a la estadía. Por defecto lanza excepción si no es posible.
     */
    public void agregarHuesped(Estadia estadia, Huesped huesped) {
        throw new IllegalStateException("No se puede agregar huésped en estado " + getNombre());
    }

    /**
     * Intenta agregar un servicio extra a la estadía. Por defecto lanza excepción si no es posible.
     */
    public void agregarServicioExtra(Estadia estadia, ServicioExtra servicio) {
        throw new IllegalStateException("No se puede agregar servicio extra en estado " + getNombre());
    }

    /**
     * Intenta generar una factura para la estadía. Por defecto lanza excepción si no es posible.
     */
    public void generarFactura(Estadia estadia, Factura factura) {
        throw new IllegalStateException("No se puede generar factura en estado " + getNombre());
    }

    /**
     * Intenta modificar las fechas de la estadía. Por defecto lanza excepción si no es posible.
     */
    public void modificarFechas(Estadia estadia, Date nuevoCheckin, Date nuevoCheckout) {
        throw new IllegalStateException("No se pueden modificar fechas en estado " + getNombre());
    }

    /**
     * Intenta finalizar la estadía (transición de ENCURSO a FINALIZADA).
     * Por defecto lanza excepción si no es posible.
     */
    public void finalizar(Estadia estadia) {
        throw new IllegalStateException("No se puede finalizar en estado " + getNombre());
    }
}

