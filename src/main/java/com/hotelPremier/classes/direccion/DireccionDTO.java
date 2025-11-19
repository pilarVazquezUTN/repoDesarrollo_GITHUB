package com.hotelPremier.classes.direccion;

public class DireccionDTO {
    private String calle;
    private String numero;
    private String localidad;
    private String departamento;
    private String piso;
    private String codigoPostal;
    private String provincia;
    private String pais;

    /**
     *
     * @param calle
     */
    public void setCalle(String calle) {
        this.calle = calle;
    }

    /**
     *
     * @param numero
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     *
     * @param localidad
     */
    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    /**
     *
     * @param departamento
     */
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    /**
     *
     * @param piso
     */
    public void setPiso(String piso) {
        this.piso = piso;
    }

    /**
     *
     * @param codigoPostal
     */
    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    /**
     *
     * @param provincia
     */
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    /**
     *
     * @param pais
     */
    public void setPais(String pais) {
        this.pais = pais;
    }

    /**
     *
     * @return retorna nombre calle
     */
    public String getCalle() {
        return this.calle;
    }

    /**
     *
     * @return retorna numero calle
     */
    public String getNumero() {
        return this.numero;
    }

    /**
     *
     * @return retorna localidad
     */
    public String getLocalidad() {
        return this.localidad;
    }

    /**
     *
     * @return retorna departamento
     */
    public String getDepartamento() {
        return this.departamento;
    }

    /**
     *
     * @return retorna piso
     */
    public String getPiso() {
        return this.piso;
    }

    /**
     *
     * @return retorna cp
     */
    public String getCodigoPostal() {
        return this.codigoPostal;
    }

    /**
     *
     * @return  retorna provincia
     */
    public String getProvincia() {
        return this.provincia;
    }

    /**
     *
     * @return retorna pais
     */
    public String getPais() {
        return this.pais;
    }    
}
