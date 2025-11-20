package com.hotelPremier.classes.relacionEstadiaHuesped;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class estadiaHuespedID implements Serializable {
    private Integer idEstadia;
    private String dni;
    private String tipoDocumento;
}
