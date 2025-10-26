package classes.factura;

import Classes.Huesped.Huesped;
import classes.reserva.Reserva;
import classes.responsablePago.ResponsablePago;

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
