/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes.Factura;

/**
 *
 * @author st
 */
public class GestorFacturacion {
    private String id_empleado;

    public void setId_empleado(String id_empleado) {
        this.id_empleado = id_empleado;
    }
    public String getId_empleado() {
        return id_empleado;
    }

//las hago aca para tenerlas pero hay q implementar!!!!!!!!!!!!!!!!!!!!!!!!!!
    public Factura generarFactura(Reserva unaReserva, ResponsablePago responsablePago) {
        return null;
    }

    public ResponsablePago seleccionarResponsablePago(Huesped unHuesped) {
        return null;
    }
    public void emitirFactura(){

    }

    public void emitirNotaDeCredito(){

    }

    public void eliminarNotadeCredito(){

    }
    public NotaDeCredito crearNotaDeCredito(){
        return null;
    }
}
