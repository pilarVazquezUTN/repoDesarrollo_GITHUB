package com.hotelPremier.classes.Dominio;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hotelPremier.classes.Dominio.servicioExtra.ServicioExtra;

import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;//!!!!

@JsonIgnoreProperties({"listahuesped", "habitacion"}) //JSIGNORE!!!!
@Entity

@Table(name="estadia")
public class Estadia { 
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_estadia")
    private Integer id_estadia;

    @Temporal(TemporalType.DATE) //guarda fecha sin hora
    @Column(name="checkin") 
    private Date checkin;

    @Temporal(TemporalType.DATE) //guarda fecha sin hora
    @Column(name="checkout")
    private Date checkout;

    @ManyToOne //de estadia a habitacion
    @JsonManagedReference//trabaja con el front
    @JsonIgnore
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
        return this.id_estadia;
    }
    public void setId_estadia(Integer id_estadia) {
        this.id_estadia = id_estadia;
    }
    public Date getCheckin() {
        return this.checkin;
    }
    public void setCheckin(Date checkin) {
        this.checkin = checkin;
    }
    public Date getCheckout() {
        return this.checkout;
    }
    public void setCheckout(Date checkout) {
        this.checkout = checkout;
    }
    public Habitacion getHabitacion() {
        return this.habitacion;
    }
    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }
    public List<Huesped> getListahuesped() {
        return this.listahuesped;

    }
    public void setListahuesped(List<Huesped> listahuesped) {
        this.listahuesped = listahuesped;
    }
}