package com.hotelPremier.classes.Dominio;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hotelPremier.classes.Dominio.estadia.estado.EstadoEstadia;
import com.hotelPremier.classes.Dominio.estadia.estado.EstadiaEnCurso;
import com.hotelPremier.classes.Dominio.estadia.estado.EstadiaFinalizada;
import com.hotelPremier.classes.Dominio.estadia.observer.EstadiaObserver;

import jakarta.persistence.*;
import java.util.ArrayList;

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

    @Transient
    private EstadoEstadia estadoEstadia;

    /**
     * Lista de observers registrados para reaccionar a cambios de estado.
     * 
     * IMPORTANTE: Este campo es @Transient y NO se persiste en la base de datos.
     * Los observers representan reacciones en tiempo de ejecución y no forman parte
     * del estado persistente del dominio. Se registran en los services y no sobreviven
     * a reinicios de la aplicación. Cada vez que se carga una estadía desde la BD,
     * la lista de observers está vacía y debe ser repoblada por el service si se requiere.
     */
    @Transient
    private List<EstadiaObserver> observers = new ArrayList<>();

    public Estadia() {
        // Inicializar con estado ENCURSO por defecto
        this.estado = "ENCURSO";
        this.estadoEstadia = new EstadiaEnCurso();
    }

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
    public void setEstado(String estado) { 
        this.estado = estado;
        syncEstado();
    }

    /**
     * Sincroniza el estadoEstadia (transient) con el estado String (persistido).
     * Se ejecuta automáticamente después de cargar desde la BD o persistir.
     */
    @PostLoad
    @PostPersist
    private void syncEstado() {
        if (estado == null || estado.isEmpty()) {
            estado = "ENCURSO"; // Estado por defecto
        }
        switch (estado.toUpperCase()) {
            case "FINALIZADA" -> estadoEstadia = new EstadiaFinalizada();
            default -> estadoEstadia = new EstadiaEnCurso();
        }
    }

    /**
     * Establece el estado de la estadía y sincroniza el String persistido.
     * Este método es usado internamente por los estados concretos.
     * Notifica a los observers SOLO cuando existe una transición efectiva de estado.
     */
    public void setEstadoEstadia(EstadoEstadia nuevoEstado) {
        String estadoAnterior = this.estado;
        this.estadoEstadia = nuevoEstado;
        this.estado = nuevoEstado.getNombre();
        
        // Notificar a los observers SOLO si el estado cambió realmente
        if (!estadoAnterior.equals(this.estado)) {
            notificarObservers();
        }
    }

    /**
     * Registra un observer para ser notificado cuando la estadía cambie de estado.
     * 
     * @param observer El observer a registrar
     */
    public void registrarObserver(EstadiaObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Elimina un observer de la lista de observadores.
     * 
     * @param observer El observer a eliminar
     */
    public void eliminarObserver(EstadiaObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifica a todos los observers registrados sobre el cambio de estado.
     */
    private void notificarObservers() {
        for (EstadiaObserver observer : observers) {
            observer.actualizar(this);
        }
    }

    /**
     * Inicia la estadía (cambia a estado ENCURSO) y notifica a los observers.
     * Usa el patrón State para validar el cambio antes de notificar.
     * TODAS las transiciones de estado se realizan exclusivamente a través de métodos del patrón State.
     * Los observers se notifican SOLO cuando existe una transición efectiva de estado.
     */
    public void iniciarEstadia() {
        // Validar que se pueda iniciar (usando State si es necesario)
        // Por defecto, una estadía nueva ya está en ENCURSO
        if (!"ENCURSO".equals(this.estado)) {
            // Si no está en ENCURSO, cambiar usando el patrón State (método centralizado)
            if (estadoEstadia == null) {
                syncEstado();
            }
            // Cambiar a ENCURSO usando el método centralizado de transición
            // setEstadoEstadia() notificará automáticamente a los observers si hay cambio
            setEstadoEstadia(new EstadiaEnCurso());
        }
        // Si ya está en ENCURSO, NO notificar observers (no hay cambio de estado)
    }

    /**
     * Método delegado: delega al estado actual la operación de agregar huésped.
     */
    public void agregarHuesped(com.hotelPremier.classes.Dominio.Huesped huesped) {
        if (estadoEstadia == null) {
            syncEstado();
        }
        estadoEstadia.agregarHuesped(this, huesped);
    }

    /**
     * Método delegado: delega al estado actual la operación de agregar servicio extra.
     */
    public void agregarServicioExtra(com.hotelPremier.classes.Dominio.servicioExtra.ServicioExtra servicio) {
        if (estadoEstadia == null) {
            syncEstado();
        }
        estadoEstadia.agregarServicioExtra(this, servicio);
    }

    /**
     * Método delegado: delega al estado actual la operación de generar factura.
     */
    public void generarFactura(Factura factura) {
        if (estadoEstadia == null) {
            syncEstado();
        }
        estadoEstadia.generarFactura(this, factura);
    }

    /**
     * Método delegado: delega al estado actual la operación de modificar fechas.
     */
    public void modificarFechas(Date nuevoCheckin, Date nuevoCheckout) {
        if (estadoEstadia == null) {
            syncEstado();
        }
        estadoEstadia.modificarFechas(this, nuevoCheckin, nuevoCheckout);
    }

    /**
     * Método delegado: delega al estado actual la operación de finalizar estadía.
     */
    public void finalizar() {
        if (estadoEstadia == null) {
            syncEstado();
        }
        estadoEstadia.finalizar(this);
    }
}
