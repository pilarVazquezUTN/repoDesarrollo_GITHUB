package classes.pago;

import classes.factura.Factura;

public class GestorPago {
    private String idEmpleado;
    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }
    public String getIdEmpleado() {
        return idEmpleado;
    }
    public void ingresarPago(Factura factura,  float monto){

    }

    public boolean validarMonto(){
        return false;
    }
    public void registrarPago(){

    }
    public boolean errorValidacion(){
        return false;
    }
}
