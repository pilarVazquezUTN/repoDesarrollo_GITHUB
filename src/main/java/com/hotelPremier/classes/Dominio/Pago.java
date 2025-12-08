package com.hotelPremier.classes.Dominio;

import java.util.Date;
import java.util.List;

import com.hotelPremier.classes.Dominio.medioDePago.MedioDePago;

import jakarta.persistence.*;

@Entity
@Table(name = "pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Integer id_pago;

    @Column(name = "monto")
    private float monto;

    @Column(name = "mediopago")
    private String medioPago;

    @Column(name = "fecha")
    private Date fecha;

    @OneToOne
    @JoinColumn(name = "factura")
    private Factura factura;

    @ManyToMany
    @JoinTable(
        name = "mediopago_pago",
        joinColumns = @JoinColumn(name = "id_pago"),
        inverseJoinColumns = @JoinColumn(name = "id_mediodepago")
    )
    private List<MedioDePago> listamediodepago;

    // ===========================
    // Constructores
    // ===========================

    public Pago() {
    }

    public Pago(Integer id_pago, float monto, String medioPago, Date fecha,
                Factura factura, List<MedioDePago> listamediodepago) {
        this.id_pago = id_pago;
        this.monto = monto;
        this.medioPago = medioPago;
        this.fecha = fecha;
        this.factura = factura;
        this.listamediodepago = listamediodepago;
    }

    // ===========================
    // Getters y Setters
    // ===========================

    public Integer getId_pago() {
        return id_pago;
    }

    public void setId_pago(Integer id_pago) {
        this.id_pago = id_pago;
    }

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

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public List<MedioDePago> getListamediodepago() {
        return listamediodepago;
    }

    public void setListamediodepago(List<MedioDePago> listamediodepago) {
        this.listamediodepago = listamediodepago;
    }
}
