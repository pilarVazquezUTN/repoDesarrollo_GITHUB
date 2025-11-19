package classes.responsablePago;

import classes.direccion.Direccion;

public class PersonaJuridicaDTO {
    private String razonSocial;
    private String cuit;
    private Direccion direccion;

    /**
     *
     * @param razonSocial
     */
    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
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
     * @param direccion
     */
    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    /**
     *
     * @return
     */
    public String getRazonSocial() {
        return razonSocial;
    }

    /**
     *
     * @return
     */
    public String getCuit() {
        return cuit;
    }

    /**
     *
     * @return
     */
    public Direccion getDireccion() {
        return direccion;
    }

}
