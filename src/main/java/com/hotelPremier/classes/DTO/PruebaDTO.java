package com.hotelPremier.classes.DTO;

public class PruebaDTO {

    private String cadena;

    // Constructor vacío requerido por Jackson
    public PruebaDTO() { }

    // Constructor opcional para usarlo a mano
    public PruebaDTO(String cadena){
        this.cadena = cadena;
    }

    public void setCadena(String cadena){
        this.cadena = cadena;
    }

    public String getCadena(){
        return this.cadena;
    }
}
