package Classes.Huesped;

import java.util.Date;

import Classes.Direccion.DireccionDTO;
import classes.estadia.EstadiaDTO;

public class HuespedDTO {
    private String apellido;
    private String nombre;
    private String tipoDocumento;
    private String numeroDocumento;
    private Date fechaNacimiento;
    private String telefono;
    private String email;
    private DireccionDTO direccionHuesped;
    private String cuit;
    private String posicionIva;
    private String ocupacion;
    private String nacionalidad;
    private classes.estadia.EstadiaDTO [] estadiaHuesped;

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
    public void setDireccionHuesped(DireccionDTO direccionHuesped) {
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
        return this.fechaNacimiento;
    }
    public String getApellido() {
        return this.apellido;
    }
    public String getTipoDocumento() {
        return this.tipoDocumento;
    }
    public String getNumeroDocumento() {
        return this.numeroDocumento;
    }
    public String getCuit() {
        return this.cuit;
    }
    public String getPosicionIva() {
        return this.posicionIva;
    }
    public String getOcupacion() {
        return this.ocupacion;
    }
    public String getNacionalidad() {
        return this.nacionalidad;
    }
    public String getEmail() {
        return this.email;
    }
    public DireccionDTO getDireccionHuesped() {
        return this.direccionHuesped;
    }
    public String getNombre() {
        return this.nombre;
    }
    public String getTelefono() {
        return this.telefono;
    }

    public classes.estadia.EstadiaDTO[] getEstadiaHuesped() {
        return estadiaHuesped;
    }
}
