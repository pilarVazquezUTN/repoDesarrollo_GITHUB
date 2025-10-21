/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes.Direccion;

/**
 *
 * @author st
 */
public class Direccion {
    private String calle;
    private String numero;
    private String localidad;
    private String departamento;
    private String piso;
    private String codigoPostal;
    private String provincia;
    private String pais;

    public void setCalle(String calle) {
        this.calle = calle;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    public void setPiso(String piso) {
        this.piso = piso;
    }
    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }




    public String getCalle() {
        return calle;
    }
    public String getNumero() {
        return numero;
    }
    public String getLocalidad() {
        return localidad;
    }
    public String getDepartamento() {
        return departamento;
    }
    public String getPiso() {
        return piso;
    }
    public String getCodigoPostal() {
        return codigoPostal;
    }
    public String getProvincia() {
        return provincia;
    }
    public String getPais() {
        return pais;
    }

}


