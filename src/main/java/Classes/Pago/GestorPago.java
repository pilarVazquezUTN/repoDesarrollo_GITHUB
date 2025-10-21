/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes.Pago;

/**
 *
 * @author st
 */
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
