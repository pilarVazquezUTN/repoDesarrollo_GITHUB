package Classes.Factura;

import java.util.Date;

public class FacturaDTO {
    private Date fecha;
    private float total;
    private String tipo;
    private String estado;

    public Date getFecha() {
        return fecha;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public float getTotal() {
        return total;
    }
    public void setTotal(float total) {
        this.total = total;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void agregarItem(){

    }
    public void cancelar(){

    }
    public void calcularTotal(){

    }
}
