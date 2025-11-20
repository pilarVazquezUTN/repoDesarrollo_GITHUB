package com.hotelPremier.classes.factura;

import java.util.Date;
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
    private Integer id_estadia;
    @ManyToOne 
    private Integer id_notacredito;
    @OneToOne  
    private Integer id_pago;

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
