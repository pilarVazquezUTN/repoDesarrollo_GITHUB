package com.hotelPremier.classes.Dominio;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hotelPremier.classes.Dominio.responsablePago.ResponsablePago;
import com.hotelPremier.classes.Dominio.factura.estado.EstadoFactura;
import com.hotelPremier.classes.Dominio.factura.estado.FacturaPendiente;
import com.hotelPremier.classes.Dominio.factura.estado.FacturaGenerada;
import com.hotelPremier.classes.Dominio.factura.estado.FacturaPagada;
import com.hotelPremier.classes.Dominio.factura.estado.FacturaCancelada;
import com.hotelPremier.classes.Dominio.factura.observer.FacturaObserver;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "factura")
public class Factura {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha")
    private Date fecha;

    @Column(name = "total")
    private float total;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "estado")
    private String estado;

    @Transient
    private EstadoFactura estadoFactura;

    /**
     * Lista de observers registrados para reaccionar a cambios de estado.
     * 
     * IMPORTANTE: Este campo es @Transient y NO se persiste en la base de datos.
     * Los observers representan reacciones en tiempo de ejecución y no forman parte
     * del estado persistente del dominio. Se registran en los services y no sobreviven
     * a reinicios de la aplicación. Cada vez que se carga una factura desde la BD,
     * la lista de observers está vacía y debe ser repoblada por el service si se requiere.
     */
    @Transient
    private List<FacturaObserver> observers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "id_estadia")
    @JsonBackReference(value = "estadia-facturas")
    private Estadia estadia;


    // FK correcta hacia NotaDeCredito
    @ManyToOne
    @JoinColumn(name = "id_notacredito")
    private NotaDeCredito notacredito;

    @OneToOne
    @JoinColumn(name = "pago")
    private Pago pago;

    @ManyToOne
    @JoinColumn(name = "responsablepago")
    private ResponsablePago responsablepago;

    public Factura() {
        // Inicializar con estado PENDIENTE por defecto
        this.estado = "PENDIENTE";
        this.estadoFactura = new FacturaPendiente();
    }

    public Factura(Date fecha, float total, String tipo, String estado,
                   Estadia estadia, NotaDeCredito notacredito, Pago pago,
                   ResponsablePago responsablepago) {
        this.fecha = fecha;
        this.total = total;
        this.tipo = tipo;
        this.estado = estado;
        this.estadia = estadia;
        this.notacredito = notacredito;
        this.pago = pago;
        this.responsablepago = responsablepago;
        // Sincronizar estadoFactura con el estado String
        syncEstado();
    }

    public Integer getID() { return ID; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public float getTotal() { return total; }
    public void setTotal(float total) { this.total = total; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { 
        this.estado = estado;
        syncEstado();
    }

    // ============================================================
    //   MÉTODOS DE DOMINIO (PATRÓN STATE)
    // ============================================================
    
    /**
     * Método delegado: delega al estado actual la operación de pagar.
     */
    public void pagar() {
        if (estadoFactura == null) {
            syncEstado();
        }
        estadoFactura.pagar(this);
    }

    /**
     * Método delegado: delega al estado actual la operación de cancelar.
     */
    public void cancelar() {
        if (estadoFactura == null) {
            syncEstado();
        }
        estadoFactura.cancelar(this);
    }

    /**
     * Método delegado: delega al estado actual la operación de aplicar nota de crédito.
     */
    public void aplicarNotaCredito() {
        if (estadoFactura == null) {
            syncEstado();
        }
        estadoFactura.aplicarNotaCredito(this);
    }

    /**
     * Genera la factura final (cambia a estado GENERADA) usando el patrón State.
     * El State valida que la transición sea válida, luego notifica a los observers.
     * 
     * Este método debe ser llamado cuando se genera la factura final de una estadía.
     * TODAS las transiciones de estado se realizan exclusivamente a través de métodos del patrón State.
     */
    public void generarFacturaFinal() {
        if (estadoFactura == null) {
            syncEstado();
        }
        
        // Validar que se pueda generar (usando State)
        // Si la factura está en PENDIENTE, puede pasar a GENERADA
        if (!"PENDIENTE".equals(this.estado)) {
            throw new IllegalStateException("Solo se puede generar una factura final desde estado PENDIENTE. Estado actual: " + this.estado);
        }
        
        // Cambiar a GENERADA usando State (método centralizado para transiciones)
        setEstadoFactura(new FacturaGenerada());
    }

    // ============================================================
    //   MÉTODOS DE OBSERVER (PATRÓN OBSERVER)
    // ============================================================
    
    /**
     * Registra un observer para ser notificado cuando la factura cambie de estado.
     * 
     * @param observer El observer a registrar
     */
    public void registrarObserver(FacturaObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Elimina un observer de la lista de observadores.
     * 
     * @param observer El observer a eliminar
     */
    public void eliminarObserver(FacturaObserver observer) {
        observers.remove(observer);
    }

    // ============================================================
    //   MÉTODOS HELPERS INTERNOS
    // ============================================================
    
    /**
     * Establece el estado de la factura y sincroniza el String persistido.
     * Este método es usado internamente por los estados concretos.
     * Notifica a los observers después del cambio de estado.
     */
    public void setEstadoFactura(EstadoFactura nuevoEstado) {
        String estadoAnterior = this.estado;
        this.estadoFactura = nuevoEstado;
        this.estado = nuevoEstado.getNombre();
        
        // Notificar a los observers si el estado cambió
        if (!estadoAnterior.equals(this.estado)) {
            notificarObservers();
        }
    }

    /**
     * Sincroniza el estadoFactura (transient) con el estado String (persistido).
     * Se ejecuta automáticamente después de cargar desde la BD o persistir.
     */
    @PostLoad
    @PostPersist
    private void syncEstado() {
        if (estado == null || estado.isEmpty()) {
            estado = "PENDIENTE";
        }
        switch (estado.toUpperCase()) {
            case "GENERADA" -> estadoFactura = new FacturaGenerada();
            case "PAGADA" -> estadoFactura = new FacturaPagada();
            case "CANCELADA" -> estadoFactura = new FacturaCancelada();
            default -> estadoFactura = new FacturaPendiente();
        }
    }

    /**
     * Notifica a todos los observers registrados sobre el cambio de estado.
     */
    private void notificarObservers() {
        for (FacturaObserver observer : observers) {
            observer.actualizar(this);
        }
    }

    // ============================================================
    //   GETTERS Y SETTERS
    // ============================================================
    
    public Estadia getEstadia() { return estadia; }
    public void setEstadia(Estadia estadia) { this.estadia = estadia; }

    public NotaDeCredito getNotaDeCredito() { return notacredito; }
    public void setNotaDeCredito(NotaDeCredito notacredito) { this.notacredito = notacredito; }

    public Pago getPago() { return pago; }
    public void setPago(Pago pago) { this.pago = pago; }

    public ResponsablePago getResponsablePago() { return responsablepago; }
    public void setResponsablePago(ResponsablePago responsablepago) {
        this.responsablepago = responsablepago;
    }
}
