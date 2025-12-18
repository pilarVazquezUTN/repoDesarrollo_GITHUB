package com.hotelPremier.exception;

/**
 * Excepción lanzada cuando un recurso solicitado no existe en el sistema.
 * Se mapea a HTTP 404 Not Found.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    public RecursoNoEncontradoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
