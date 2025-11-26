package com.hotelPremier.classes.habitacion;

 import java.util.List;

import com.hotelPremier.classes.reserva.Reserva;
import com.hotelPremier.classes.estadia.Estadia;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+

@Entity

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipohabitacion")

@Table(name="habitacion")
public class Habitacion  {
    @Id 
    private int numero; 

    @Column(name="estado") 
    private String estado;
    @Column(name="precio")  
    private float precio;
    @Column(name="cantidadpersonas")
    private int cantidadPersonas;    
    @Column(name = "tipohabitacion", insertable=false, updatable=false)
    private String tipohabitacion;

    @OneToMany(mappedBy = "habitacion")
    private List<Estadia> listaestadias;



    // GETTERS y SETTERS

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(int numero) { this.numero = numero; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public float getPrecio() { return precio; }
    public void setPrecio(float precio) { this.precio = precio; }

    public int getCantidadPersonas() { return cantidadPersonas; }
    public void setCantidadPersonas(int cantidadPersonas) { this.cantidadPersonas = cantidadPersonas; }


    public void setTipo(String tipo){
        
        this.tipohabitacion=tipo;
        /* 
        if (tipo.equals("IndividualEstandar")) this.tipoHab="IndividualEstandar";
        if (tipo.equals("DobleEstandar") ) this.tipoHab="DobleEstandar";
        if (this instanceof DobleSuperior) this.tipoHab="DobleSuperior";
        if (this instanceof SuperiorFamilyPlan) this.tipoHab="SuperiorFamilyPlan";
        if (this instanceof Suite) this.tipoHab="Suite";
        */
    }
    public String getTipo(){
        if (this instanceof IndividualEstandar) return "IndividualEstandar";
        if (this instanceof DobleEstandar) return "DobleEstandar";
        if (this instanceof DobleSuperior) return "DobleSuperior";
        if (this instanceof SuperiorFamilyPlan) return "SuperiorFamilyPlan";
        if (this instanceof Suite) return "Suite";
        
        return null;
    }
} 
