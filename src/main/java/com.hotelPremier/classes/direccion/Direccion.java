package classes.direccion;

import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+

@Entity
@Table(name="direccion")
public class Direccion {
    private String calle;
    private String numero;
    private String localidad;
    private String departamento;
    private String piso;
    private String codigoPostal;
    private String provincia;
    private String pais;

    /**
     * constructor de direccion
     * @param calle
     * @param numero
     * @param localidad
     * @param departamento
     * @param piso
     * @param codigoPostal
     * @param provincia
     * @param pais
     */
    public Direccion(String calle, String numero, String localidad, String departamento, String piso, String codigoPostal, String provincia, String pais) {
        this.calle = calle;
        this.numero = numero;
        this.localidad = localidad;
        this.departamento = departamento;
        this.piso = piso;
        this.codigoPostal = codigoPostal;
        this.provincia = provincia;
        this.pais = pais;
    }

    /**
     * retorna la calle
     * @return
     */
    public String getCalle() {
        return calle;
    }

    /**
     * retorna el numero
     * @return
     */
    public String getNumero() {
        return numero;
    }

    /**
     * retorna localidad
     * @return
     */
    public String getLocalidad() {
        return localidad;
    }
    /**
     * retorna departamento
     * @return
     */
    public String getDepartamento() {
        return departamento;
    }
    /**
     * retorna piso
     * @return
     */
    public String getPiso() {
        return piso;
    }

    /**
     * retorna cp
     * @return
     */
    public String getCodigoPostal() {
        return codigoPostal;
    }
    /**
     * retorna provincia
     * @return
     */
    public String getProvincia() {
        return provincia;
    }
    /**
     * retorna pais
     * @return
     */
    public String getPais() {
        return pais;
    }

    /**
     * set de calle
     * @param calle
     */
    public void setCalle(String calle) {
        this.calle = calle;
    }

    /**
     * set de numero
     * @param numero
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * set de localidad
     * @param localidad
     */
    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    /**set de departamento
     *
     * @param departamento
     */
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    /**
     * set de piso
     * @param piso
     */
    public void setPiso(String piso) {
        this.piso = piso;
    }

    /**
     * set de codigo postal
     * @param codigoPostal
     */
    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    /**
     * set de provincia
     * @param provincia
     */
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    /**
     * set d pais
     * @param pais
     */
    public void setPais(String pais) {
        this.pais = pais;
    }
}


