package com.hotelPremier.classes.medioDePago;

import java.util.Date;
import java.util.List;

import com.hotelPremier.classes.pago.Pago;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+

@Entity
@Table(name="medio_pago")
public class MedioDePago {
    @Id 
    @Column(name="id_mediodepago")
    private Integer id_mediodepago;
    @Column(name="monto")
    private int monto;
    @Column(name="fecha")
    private Date fecha;

    private List<Pago> listaPago;

    public void setIdMedioDePago(Integer id_mediodepago){
        this.id_mediodepago=id_mediodepago;
    }
}
