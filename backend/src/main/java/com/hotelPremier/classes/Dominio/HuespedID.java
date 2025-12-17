package com.hotelPremier.classes.Dominio;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class HuespedID implements Serializable {

    @Column(name = "tipodocumento")
    private String tipoDocumento;

    @Column(name = "dni")
    private String dni;

    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HuespedID)) return false;
        HuespedID that = (HuespedID) o;
        return Objects.equals(tipoDocumento, that.tipoDocumento) &&
               Objects.equals(dni, that.dni);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipoDocumento, dni);
    }
}
