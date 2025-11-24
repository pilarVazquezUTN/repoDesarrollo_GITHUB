package com.hotelPremier.classes.estadia;

import java.util.Date;
import java.util.List;
import com.hotelPremier.classes.habitacion.Habitacion;
import com.hotelPremier.classes.huesped.Huesped;
import com.hotelPremier.classes.servicioExtra.ServicioExtra;

import com.hotelPremier.classes.factura.Factura;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+
import jakarta.websocket.OnMessage;

@Entity
@Table(name="estadia")
public class Estadia { 
    @Id
    @Column(name="id_estadia")
    private Integer id_estadia;
    @Column(name="checkin")
    private Date checkin;
    @Column(name="checkout")
    private Date checkout;

    @ManyToOne //de estadia a habitacion
    @JoinColumn(name = "nro_habitacion")
    private Habitacion habitacion;

    @ManyToMany(mappedBy = "listaestadia")
    private List<Huesped> listahuesped;

    @ManyToMany(mappedBy = "listaestadia")
    private List<Factura> listafactura;

    @OneToMany(mappedBy = "estadia")
    private List<ServicioExtra> listaserviciosextra;



    


    public Integer getId_estadia() {
        return id_estadia;
    }
    public void setId_estadia(Integer id_estadia) {
        this.id_estadia = id_estadia;
    }

}