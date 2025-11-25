package com.hotelPremier.classes.huesped;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
@Embeddable
public class HuespedID implements Serializable {
    @Column(name = "tipodocumento") 
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
 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HuespedID that = (HuespedID) o;
        return tipoDocumento.equals(that.tipoDocumento) && dni.equals(that.dni);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(tipoDocumento, dni);
    }
}
