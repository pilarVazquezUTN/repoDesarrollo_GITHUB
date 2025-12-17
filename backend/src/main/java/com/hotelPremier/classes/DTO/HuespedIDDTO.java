package com.hotelPremier.classes.DTO;

public class HuespedIDDTO {

    private String tipoDocumento;
    private String dni;

    public HuespedIDDTO() {}

    public HuespedIDDTO(String tipoDocumento, String dni) {
        this.tipoDocumento = tipoDocumento;
        this.dni = dni;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
}
