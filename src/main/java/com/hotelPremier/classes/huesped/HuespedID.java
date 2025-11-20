package com.hotelPremier.classes.huesped;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
@Embeddable
public class HuespedID implements Serializable {

    private String tipoDocumento;

   @Column(name = "dni")
   private String dni;

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
    public String getTipoDocumento() {
        return tipoDocumento;
    }
    public void setDni(String dni) {
        this.dni = dni;
    }
    public String getDni() {
        return dni;
    }

}
