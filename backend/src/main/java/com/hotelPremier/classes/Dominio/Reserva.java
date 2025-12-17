package com.hotelPremier.classes.Dominio;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hotelPremier.classes.Dominio.reserva.estado.EstadoReserva;
import com.hotelPremier.classes.Dominio.reserva.estado.ReservaPendiente;
import com.hotelPremier.classes.Dominio.reserva.estado.ReservaConsumida;
import com.hotelPremier.classes.Dominio.reserva.estado.ReservaCancelada;

import jakarta.persistence.*;

@Entity
@Table(name = "reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_reserva")
    private Integer id_reserva;

    @Temporal(TemporalType.DATE)
    @Column(name="fecha_desde")
    private Date fecha_desde;

    @Temporal(TemporalType.DATE)
    @Column(name="fecha_hasta")
    private Date fecha_hasta;

    @Column(name="estado")
    private String estado;

    @Transient
    private EstadoReserva estadoReserva;

    @Column(name="nombre")
    private String nombre;

    @Column(name="apellido")
    private String apellido;

    @Column(name="telefono")
    private String telefono;

    // ===============================
    // HABITACIÓN (lado BACKREF)
    // ===============================
    @ManyToOne
    @JoinColumn(name = "nro_habitacion")
    @JsonBackReference(value = "habitacion-reservas")
    private Habitacion habitacion;

    // ===============================
    // ESTADIA (lado MANAGED)
    // ===============================
    @OneToOne(mappedBy = "reserva")
    @JsonManagedReference(value = "reserva-estadia")
    private Estadia estadia;

    public Reserva() {
        // Inicializar con estado PENDIENTE por defecto
        this.estado = "PENDIENTE";
        this.estadoReserva = new ReservaPendiente();
    }

    public Reserva(
            Integer id_reserva,
            Date fecha_desde,
            Date fecha_hasta,
            String estado,
            String nombre,
            String apellido,
            String telefono,
            Habitacion habitacion,
            Estadia estadia
    ) {
        this.id_reserva = id_reserva;
        this.fecha_desde = fecha_desde;
        this.fecha_hasta = fecha_hasta;
        this.estado = estado;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.habitacion = habitacion;
        this.estadia = estadia;
        // Sincronizar estadoReserva con el estado String
        syncEstado();
    }

    // ========================
    // GETTERS / SETTERS
    // ========================

    public Integer getId_reserva() { return id_reserva; }
    public void setId_reserva(Integer id_reserva) { this.id_reserva = id_reserva; }

    public Date getFecha_desde() { return fecha_desde; }
    public void setFecha_desde(Date fecha_desde) { this.fecha_desde = fecha_desde; }

    public Date getFecha_hasta() { return fecha_hasta; }
    public void setFecha_hasta(Date fecha_hasta) { this.fecha_hasta = fecha_hasta; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { 
        this.estado = estado;
        syncEstado();
    }

    /**
     * Sincroniza el estadoReserva (transient) con el estado String (persistido).
     * Se ejecuta automáticamente después de cargar desde la BD o persistir.
     */
    @PostLoad
    @PostPersist
    private void syncEstado() {
        if (estado == null || estado.isEmpty()) {
            estado = "PENDIENTE"; // Estado por defecto
        }
        switch (estado.toUpperCase()) {
            case "CONSUMIDA" -> estadoReserva = new ReservaConsumida();
            case "CANCELADA" -> estadoReserva = new ReservaCancelada();
            default -> estadoReserva = new ReservaPendiente();
        }
    }

    /**
     * Establece el estado de la reserva y sincroniza el String persistido.
     * Este método es usado internamente por los estados concretos.
     */
    public void setEstadoReserva(EstadoReserva nuevoEstado) {
        this.estadoReserva = nuevoEstado;
        this.estado = nuevoEstado.getNombre();
    }

    /**
     * Método delegado: delega al estado actual la operación de cancelar.
     */
    public void cancelar() {
        if (estadoReserva == null) {
            syncEstado();
        }
        estadoReserva.cancelar(this);
    }

    /**
     * Método delegado: delega al estado actual la operación de hacer check-in.
     * Consume la reserva y crea una nueva Estadia en estado ENCURSO.
     * 
     * @return La nueva Estadia creada
     */
    public Estadia checkIn() {
        if (estadoReserva == null) {
            syncEstado();
        }
        return estadoReserva.checkIn(this);
    }

    /**
     * Método delegado: delega al estado actual la operación de consumir la reserva.
     * Cambia el estado a CONSUMIDA sin crear una nueva estadía.
     * Útil cuando la estadía ya existe y solo se necesita marcar la reserva como consumida.
     */
    public void consumir() {
        if (estadoReserva == null) {
            syncEstado();
        }
        estadoReserva.consumir(this);
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Habitacion getHabitacion() { return habitacion; }
    public void setHabitacion(Habitacion habitacion) { this.habitacion = habitacion; }

    public Estadia getEstadia() { return estadia; }
    public void setEstadia(Estadia estadia) { this.estadia = estadia; }
}
