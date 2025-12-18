package com.hotelPremier.exception;

/**
 * Excepción lanzada cuando una operación viola una regla de negocio.
 * Se mapea a HTTP 409 Conflict.
 */
public class NegocioException extends RuntimeException {

    public NegocioException(String mensaje) {
        super(mensaje);
    }

    public NegocioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
