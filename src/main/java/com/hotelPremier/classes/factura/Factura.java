package com.hotelPremier.classes.factura;

import java.util.Date;

import com.hotelPremier.classes.estadia.Estadia;
import com.hotelPremier.classes.notadecredito.NotaDeCredito;
import com.hotelPremier.classes.pago.Pago;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+

@Entity
@Table(name="factura")
public class Factura {
    @Id 
    private Integer nro_factura;
    private Date fecha;
    private float total;
    private String tipo;
    private String estado;
    @ManyToOne
    @JoinColumn(name = "id_estadia") // nombre de la columna FK
    private Estadia estadia;

    @ManyToOne
    @JoinColumn(name = "id_notacredito")
    private NotaDeCredito notaCredito;

    @OneToOne
    @JoinColumn(name = "id_pago")
    private Pago pago;
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
