package com.hotelPremier.classes.Dominio;

import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "nota_credito")
public class NotaDeCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notacredito")
    private Integer id;

    @Column(name = "importe")
    private float importe;

    // 🔥 mappedBy correcto → coincide EXACTO con el atributo en Factura
    @OneToMany(mappedBy = "notacredito")
    private List<Factura> facturas;

    public Integer getId() { return id; }
    public float getImporte() { return importe; }
    public List<Factura> getFacturas() { return facturas; }

    public void setImporte(float importe) { this.importe = importe; }
    public void setFacturas(List<Factura> facturas) { this.facturas = facturas; }
}
