package com.hotelPremier.classes.DTO;

import java.time.LocalDateTime;

/**
 * DTO para respuestas de error de la API.
 * Proporciona información estructurada sobre los errores sin exponer detalles técnicos.
 */
public class ErrorResponse {

    private String mensaje;
    private LocalDateTime timestamp;
    private int status;

    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(String mensaje, int status) {
        this();
        this.mensaje = mensaje;
        this.status = status;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
