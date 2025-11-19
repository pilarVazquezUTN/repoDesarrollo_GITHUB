package com.hotelPremier.classes.factura;

import java.util.Date;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+

@Entity
@Table(name="factura")
public class Factura {
    private Date fecha;
    private float total;
    private String tipo;
    private String estado;

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
