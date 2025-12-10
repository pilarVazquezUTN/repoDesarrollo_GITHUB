package com.hotelPremier.classes.Dominio;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name="estadia")
public class Estadia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_estadia")
    private Integer id_estadia;

    @Temporal(TemporalType.DATE)
    @Column(name="checkin")
    private Date checkin;

    @Temporal(TemporalType.DATE)
    @Column(name="checkout")
    private Date checkout;

    // ✔ Mucho cuidado: HABITACION → listaEstadias es Managed, así que acá va BackReference
    @ManyToOne
    @JoinColumn(name = "nro_habitacion")
    @JsonBackReference(value = "habitacion-estadias")
    private Habitacion habitacion;

    // ✔ Estadia es dueño de los HUÉSPEDES, así que va Managed
    @ManyToMany(mappedBy = "listaEstadia", fetch = FetchType.EAGER)
    @JsonManagedReference(value = "estadia-huespedes")
    private List<Huesped> listahuesped;

    // ✔ Estadia es dueño de las Facturas → Managed
    @OneToMany(mappedBy = "estadia")
    @JsonManagedReference(value = "estadia-facturas")
    private List<Factura> listafactura;

    // ✔ Reserva es dueño de Estadia → acá va BackReference
    @OneToOne
    @JoinColumn(name = "id_reserva")
    @JsonBackReference(value = "reserva-estadia")
    private Reserva reserva;

    @Column(name="estado")
    private String estado;

    // GETTERS & SETTERS
    public Integer getId_estadia() { return id_estadia; }
    public void setId_estadia(Integer id_estadia) { this.id_estadia = id_estadia; }

    public Date getCheckin() { return checkin; }
    public void setCheckin(Date checkin) { this.checkin = checkin; }

    public Date getCheckout() { return checkout; }
    public void setCheckout(Date checkout) { this.checkout = checkout; }

    public Habitacion getHabitacion() { return habitacion; }
    public void setHabitacion(Habitacion habitacion) { this.habitacion = habitacion; }

    public List<Huesped> getListahuesped() { return listahuesped; }
    public void setListahuesped(List<Huesped> listahuesped) { this.listahuesped = listahuesped; }

    public List<Factura> getListafactura() { return listafactura; }
    public void setListafactura(List<Factura> listafactura) { this.listafactura = listafactura; }

    public Reserva getReserva() { return reserva; }
    public void setReserva(Reserva reserva) { this.reserva = reserva; }

    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
