/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author st
 */
public class Huesped {
    private String apellido;
    private String nombre;
    private String tipoDocumento;
    private String numeroDocumento;
    private Date fechaNacimiento;
    private String telefono;
    private String email;
    private Direccion direccionHuesped;
    private String cuit;
    private String posicionIva;
    private String ocupacion;
    private String nacionalidad;

    public void setNombre(String nombre) {

        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {

        this.fechaNacimiento = fechaNacimiento;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDireccionHuesped(Direccion direccionHuesped) {
        this.direccionHuesped = direccionHuesped;
    }
    public void setCuit(String cuit) {
        this.cuit = cuit;
    }
    public void setPosicionIva(String posicionIva) {
        this.posicionIva = posicionIva;
    }
    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }
    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;

    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }
    public String getApellido() {
        return apellido;
    }
    public String getTipoDocumento() {
        return tipoDocumento;
    }
    public String getNumeroDocumento() {
        return numeroDocumento;
    }
    public String getCuit() {
        return cuit;
    }
    public String getPosicionIva() {
        return posicionIva;
    }
    public String getOcupacion() {
        return ocupacion;
    }
    public String getNacionalidad() {
        return nacionalidad;
    }

    public String getEmail() {
        return email;
    }
    public Direccion getDireccionHuesped() {
        return direccionHuesped;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {

        return telefono;
    }

    public boolean esMayorDeEdad(Huesped huesped) {
        Date fechaNacimiento = huesped.getFechaNacimiento();

        Calendar hoy = Calendar.getInstance();
        Calendar nacimiento = Calendar.getInstance();
        nacimiento.setTime(fechaNacimiento);

        int edad = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR);

        // Si todavía no cumplió , restamos 1 porq todavia no complio años pero en la resta da 18
        if (hoy.get(Calendar.MONTH) < nacimiento.get(Calendar.MONTH) ||
                ((hoy.get(Calendar.MONTH) == nacimiento.get(Calendar.MONTH) &&
                        hoy.get(Calendar.DAY_OF_MONTH) < nacimiento.get(Calendar.DAY_OF_MONTH)))){
            edad--;
        }

        return edad >= 18;
    }


}


