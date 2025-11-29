package com.hotelPremier.classes.estadia;

import java.util.Date;
import java.util.List;
import com.hotelPremier.classes.habitacion.Habitacion;
import com.hotelPremier.classes.huesped.Huesped;
import com.hotelPremier.classes.reserva.Reserva;
import com.hotelPremier.classes.servicioExtra.ServicioExtra;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hotelPremier.classes.factura.Factura;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;//!!!!

@JsonIgnoreProperties({"listahuesped", "habitacion"}) //JSIGNORE!!!!
@Entity
@Table(name="estadia")
public class Estadia { 
    @Id
    @Column(name="id_estadia")
    private Integer id_estadia;

    @Temporal(TemporalType.DATE) //guarda fecha sin hora
    @Column(name="checkin") 
    private Date checkin;

    @Temporal(TemporalType.DATE) //guarda fecha sin hora
    @Column(name="checkout")
    private Date checkout;

    @ManyToOne //de estadia a habitacion
    @JsonManagedReference
    @JoinColumn(name = "nro_habitacion")
    private Habitacion habitacion;

    @ManyToMany(mappedBy = "listaestadia")
    private List<Huesped> listahuesped;

    @OneToMany(mappedBy = "estadia")
    private List<Factura> listafactura;

    @OneToMany(mappedBy = "estadia")
    private List<ServicioExtra> listaserviciosextra;

    @OneToOne
    @JoinColumn(name = "id_reserva")
    private Reserva reserva;



    


    public Integer getId_estadia() {
        return id_estadia;
    }
    public void setId_estadia(Integer id_estadia) {
        this.id_estadia = id_estadia;
    }
    public Date getCheckin() {
        return checkin;
    }
    public void setCheckin(Date checkin) {
        this.checkin = checkin;
    }
    public Date getCheckout() {
        return checkout;
    }
    public void setCheckout(Date checkout) {
        this.checkout = checkout;
    }
    public Habitacion getHabitacion() {
        return habitacion;
    }
    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }
    public List<Huesped> getListahuesped() {
        return listahuesped;

    }
    public void setListahuesped(List<Huesped> listahuesped) {
        this.listahuesped = listahuesped;
    }
}