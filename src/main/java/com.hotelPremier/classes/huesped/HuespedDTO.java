package classes.huesped;

import java.util.Date;

import classes.direccion.DireccionDTO;

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
    private Classes.Estadia.EstadiaDTO [] estadiaHuesped;

    /**
     *
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     *
     * @param apellido
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     *
     * @param tipoDocumento
     */
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
     *
     * @param numeroDocumento
     */
    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    /**
     *
     * @param fechaNacimiento
     */
    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     *
     * @param telefono
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @param direccionHuesped
     */
    public void setDireccionHuesped(DireccionDTO direccionHuesped) {
        this.direccionHuesped = direccionHuesped;
    }

    /**
     *
     * @param cuit
     */
    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    /**
     *
     * @param posicionIva
     */
    public void setPosicionIva(String posicionIva) {
        this.posicionIva = posicionIva;
    }

    /**
     *
     * @param ocupacion
     */
    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    /**
     *
     * @param nacionalidad
     */
    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    /**
     *
     * @return
     */
    public Date getFechaNacimiento() {
        return this.fechaNacimiento;
    }

    /**
     *
     * @return
     */
    public String getApellido() {
        return this.apellido;
    }

    /**
     *
     * @return
     */
    public String getTipoDocumento() {
        return this.tipoDocumento;
    }

    /**
     *
     * @return
     */
    public String getNumeroDocumento() {
        return this.numeroDocumento;
    }

    /**
     *
     * @return
     */
    public String getCuit() {
        return this.cuit;
    }

    /**
     *
     * @return
     */
    public String getPosicionIva() {
        return this.posicionIva;
    }

    /**
     *
     * @return
     */
    public String getOcupacion() {
        return this.ocupacion;
    }

    /**
     *
     * @return
     */
    public String getNacionalidad() {
        return this.nacionalidad;
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return this.email;
    }

    /**
     *
     * @return
     */
    public DireccionDTO getDireccionHuesped() {
        return this.direccionHuesped;
    }

    /**
     *
     * @return
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     *
     * @return
     */
    public String getTelefono() {
        return this.telefono;
    }

    /**
     *
     * @return
     */
    public Classes.Estadia.EstadiaDTO[] getEstadiaHuesped() {
        return estadiaHuesped;
    }
}
