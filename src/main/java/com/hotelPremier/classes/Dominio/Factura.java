package com.hotelPremier.classes.Dominio;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hotelPremier.classes.Dominio.responsablePago.ResponsablePago;
import jakarta.persistence.*;

@Entity
@Table(name = "factura")
public class Factura {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha")
    private Date fecha;

    @Column(name = "total")
    private float total;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "estado")
    private String estado;

    // ⭐ FACTURA → ESTADIA COMPLETA (SE DEVUELVE)
    @ManyToOne
    @JoinColumn(name = "id_estadia")
    @JsonManagedReference(value = "factura-estadia")
    private Estadia estadia;

    // FK correcta hacia NotaDeCredito
    @ManyToOne
    @JoinColumn(name = "id_notacredito")
    private NotaDeCredito notacredito;

    @OneToOne
    @JoinColumn(name = "pago")
    private Pago pago;

    @ManyToOne
    @JoinColumn(name = "responsablepago")
    private ResponsablePago responsablepago;

    public Factura() {}

    public Factura(Date fecha, float total, String tipo, String estado,
                   Estadia estadia, NotaDeCredito notacredito, Pago pago,
                   ResponsablePago responsablepago) {
        this.fecha = fecha;
        this.total = total;
        this.tipo = tipo;
        this.estado = estado;
        this.estadia = estadia;
        this.notacredito = notacredito;
        this.pago = pago;
        this.responsablepago = responsablepago;
    }

    public Integer getID() { return ID; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public float getTotal() { return total; }
    public void setTotal(float total) { this.total = total; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Estadia getEstadia() { return estadia; }
    public void setEstadia(Estadia estadia) { this.estadia = estadia; }

    public NotaDeCredito getNotaDeCredito() { return notacredito; }
    public void setNotaDeCredito(NotaDeCredito notacredito) { this.notacredito = notacredito; }

    public Pago getPago() { return pago; }
    public void setPago(Pago pago) { this.pago = pago; }

    public ResponsablePago getResponsablePago() { return responsablepago; }
    public void setResponsablePago(ResponsablePago responsablepago) {
        this.responsablepago = responsablepago;
    }
}
