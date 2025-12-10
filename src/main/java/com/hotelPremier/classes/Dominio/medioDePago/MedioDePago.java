package com.hotelPremier.classes.Dominio.medioDePago;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotelPremier.classes.Dominio.Pago;

import jakarta.persistence.*;

@Entity
@Table(name = "medio_pago")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_medio", discriminatorType = DiscriminatorType.STRING)
public abstract class MedioDePago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mediodepago")
    private Integer id;

    @Column(name = "monto")
    private float monto;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha")
    private Date fecha;

    @ManyToMany(mappedBy = "listamediodepago")
    @JsonIgnore
    private List<Pago> pagos;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }
}
