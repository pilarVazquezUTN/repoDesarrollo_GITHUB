package com.hotelPremier.classes.DTO;

public class UsuarioDTO {
    String nombre;
    String contrasena;

        
    public void setNombre(String unNombre){
        this.nombre= unNombre;
    }
    public void setContrasena(String unaContrasena){
        this.contrasena= unaContrasena;
    }

    public String getNombre(){
        return this.nombre;
    }
    public String getContrasena(){
        return this.contrasena;
    }
}
