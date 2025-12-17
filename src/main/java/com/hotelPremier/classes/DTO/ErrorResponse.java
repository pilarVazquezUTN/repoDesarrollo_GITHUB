package com.hotelPremier.classes.DTO;

/**
 * Clase DTO para respuestas de error estructuradas.
 * Permite que el frontend reciba mensajes de error de forma consistente.
 */
public class ErrorResponse {
    
    private String mensaje;
    private String error;
    
    public ErrorResponse() {
    }
    
    public ErrorResponse(String mensaje) {
        this.mensaje = mensaje;
        this.error = mensaje;
    }
    
    public ErrorResponse(String mensaje, String error) {
        this.mensaje = mensaje;
        this.error = error;
    }
    
    public String getMensaje() {
        return mensaje;
    }
    
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
}
