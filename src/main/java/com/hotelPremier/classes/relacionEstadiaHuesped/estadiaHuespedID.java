package com.hotelPremier.classes.relacionEstadiaHuesped;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class estadiaHuespedID implements Serializable {
    @Column(name = "id_estadia")
    private Integer idEstadia;
    @Column(name = "dni")
    private String dni;
    @Column(name = "tipoDocumento")
    private String tipoDocumento;

    // Getters y Setters
    public Integer getIdEstadia() {
        return idEstadia;
    }

    public void setIdEstadia(Integer idEstadia) {
        this.idEstadia = idEstadia;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    // equals y hashCode obligatorios para @EmbeddedId
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof estadiaHuespedID)) return false;
        estadiaHuespedID that = (estadiaHuespedID) o;
        return Objects.equals(idEstadia, that.idEstadia) &&
               Objects.equals(dni, that.dni) &&
               Objects.equals(tipoDocumento, that.tipoDocumento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEstadia, dni, tipoDocumento);
    }
}
