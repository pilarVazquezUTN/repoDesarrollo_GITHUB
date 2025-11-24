package com.hotelPremier.classes.direccion;

public class DireccionDTO {
    private Integer id_direccionDTO;
    private String calle;
    private Integer numero;
    private String localidad;
    private String departamento;
    private Integer piso;
    private Integer codigoPostal;
    private String provincia;
    private String pais;

    public void setID(Integer id){
        this.id_direccionDTO=id;
    }
    public Integer getID(){
        return this.id_direccionDTO;
    }

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
    public void setNumero(Integer numero) {
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
    public void setPiso(Integer piso) {
        this.piso = piso;
    }

    /**
     *
     * @param codigoPostal
     */
    public void setCodigoPostal(Integer codigoPostal) {
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
    public Integer getNumero() {
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
    public Integer getPiso() {
        return this.piso;
    }

    /**
     *
     * @return retorna cp
     */
    public Integer getCodigoPostal() {
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
