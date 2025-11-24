package com.hotelPremier.classes.factura;

import java.util.Date;
import java.util.List;

import com.hotelPremier.classes.estadia.Estadia;
import com.hotelPremier.classes.notadecredito.NotaDeCredito;
import com.hotelPremier.classes.pago.Pago;
import com.hotelPremier.classes.responsablePago.ResponsablePago;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+

@Entity
@Table(name="factura")
public class Factura {
    @Id 
    @Column(name="nro_factura") 
    private Integer nro_factura;

    @Temporal(TemporalType.DATE) //guarda fecha sin hora
    @Column(name="fecha")
    private Date fecha;

    @Column(name="total")
    private float total;
    @Column(name="tipo")
    private String tipo;
    @Column(name="estado")
    private String estado;
    
    
    
    @ManyToMany
    @JoinTable(
        name = "relac_estadias_facturas",  // nombre de la columna FK
        joinColumns = @JoinColumn(name = "nro_factura"),
        inverseJoinColumns = @JoinColumn(name= "id_estadia")
    )
    private List<Estadia> listaestadia;

    @ManyToOne
    @JoinColumn(name = "id_notacredito")
    private NotaDeCredito notacredito;

    @OneToMany(mappedBy = "factura")
    private List<Pago> listapago;

    @ManyToOne //(mappedBy = "nro_factura")
    @JoinColumn(name="responsablepago")  
    private ResponsablePago responsablepago;




    /**
     * Agrega un consumo a la facutura
     */
    public void agregarItem(){

    }

    /**
     *Cancela la factura
     */
    public void cancelar(){

    }

    /**
     * Calcula el total de la factura
     */

    public void calcularTotal(){

    }
}
