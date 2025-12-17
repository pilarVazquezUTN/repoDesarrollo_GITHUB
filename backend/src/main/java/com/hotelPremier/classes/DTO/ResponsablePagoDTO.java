package com.hotelPremier.classes.DTO;

public class ResponsablePagoDTO {

    private Integer id;

    private String tipo; // "FISICA" o "JURIDICA"

    // Persona física
    private String dni;
    private String tipoDocumento;

    // Persona jurídica
    private String cuit;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public String getCuit() { return cuit; }
    public void setCuit(String cuit) { this.cuit = cuit; }
}
