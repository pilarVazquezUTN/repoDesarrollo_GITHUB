package classes.responsablePago;

import classes.direccion.Direccion;

public class PersonaJuridicaDTO {
    private String razonSocial;
    private String cuit;
    private Direccion direccion;

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }
    public void setCuit(String cuit) {
        this.cuit = cuit;
    }
    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }
    public String getRazonSocial() {
        return razonSocial;
    }
    public String getCuit() {
        return cuit;
    }
    public Direccion getDireccion() {
        return direccion;
    }

}
