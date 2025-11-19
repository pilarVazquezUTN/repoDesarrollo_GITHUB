package com.hotelPremier.classes.factura;

import com.hotelPremier.classes.huesped.Huesped;
import com.hotelPremier.classes.reserva.Reserva;
import com.hotelPremier.classes.responsablePago.ResponsablePago;

public class GestorFacturacion {
    private String id_empleado;

    public void setId_empleado(String id_empleado) {
        this.id_empleado = id_empleado;
    }
    public String getId_empleado() {
        return id_empleado;
    }



    /**
     * se genera una factura
     * @param unaReserva
     * @param responsablePago
     * @return
     */
    public Factura generarFactura(Reserva unaReserva, ResponsablePago responsablePago) {
        return null;
    }

    /**
     * se selecciona al resp de pago
     * @param unHuesped
     * @return
     */
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
