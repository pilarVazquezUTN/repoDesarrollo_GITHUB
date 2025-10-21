package Classes.Pago;

import java.util.Date;

public class PagoDTO {
    private float monto;
    private String medioPago;
    private Date  fecha;



    public float getMonto() {
        return monto;
    }
    public void setMonto(float monto) {
        this.monto = monto;
    }
    public String getMedioPago() {
        return medioPago;
    }
    public void setMedioPago(String medioPago) {
        this.medioPago = medioPago;
    }
    public Date getFecha() {
        return fecha;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
