package com.hotelPremier.classes.DTO;

import java.util.Date;

public class HuespedDTO {

    private String nombre;
    private String apellido;
    private HuespedIDDTO huespedID;
    private Date fechaNacimiento;
    private String telefono;
    private String email;
    private DireccionDTO direccion;
    private String cuit;
    private String posicionIva;
    private String ocupacion;
    private String nacionalidad;

    public HuespedDTO() { this.huespedID = new HuespedIDDTO(); }

    public HuespedIDDTO getHuespedID() { return huespedID; }
    public void setHuespedID(HuespedIDDTO id){ this.huespedID = id; }

    public String getDni() { return huespedID.getDni(); }
    public String getTipoDocumento() { return huespedID.getTipoDocumento(); }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public Date getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(Date fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public DireccionDTO getDireccion() { return direccion; }
    public void setDireccion(DireccionDTO direccion) { this.direccion = direccion; }

    public String getCuit() { return cuit; }
    public void setCuit(String cuit) { this.cuit = cuit; }

    public String getPosicionIva() { return posicionIva; }
    public void setPosicionIva(String posicionIva) { this.posicionIva = posicionIva; }

    public String getOcupacion() { return ocupacion; }
    public void setOcupacion(String ocupacion) { this.ocupacion = ocupacion; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }
}
