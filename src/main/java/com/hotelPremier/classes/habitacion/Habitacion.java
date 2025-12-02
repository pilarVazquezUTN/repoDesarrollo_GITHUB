package com.hotelPremier.classes.habitacion;

 import java.util.List;

 import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
 import com.hotelPremier.classes.reserva.Reserva;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotelPremier.classes.estadia.Estadia;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+

@Entity
//@JsonIgnoreProperties({ "listareservas", "listaestadias" })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)//una clase padre tiene subclases hijas, y querés que todas se guarden juntas en una sola tabla en la base de datos.
@DiscriminatorColumn(name = "tipohabitacion")

@Table(name="habitacion")
public class Habitacion  {
    @Id 
    private Integer numero; 

    @Column(name="estado") 
    private String estado;
    @Column(name="precio")  
    private float precio;
    @Column(name="cantidadpersonas")
    private Integer cantidadPersonas;    
    @Column(name = "tipohabitacion", insertable=false, updatable=false)
    private String tipohabitacion;

    @OneToMany(mappedBy = "habitacion")
    @JsonBackReference
    @JsonIgnore
    private List<Estadia> listaestadias; 

    @OneToMany(mappedBy = "nro_habitacion")
    @JsonIgnore
    private List<Reserva> listareservas;



    
    // GETTERS y SETTERS

    
    public List<Reserva> getListareservas(){ return this.listareservas;}
    public void setListareservas(List<Reserva> listres){
        this.listareservas=listres;
    }

    public List<Estadia> getListaEstadia() { return this.listaestadias; }
    public Integer getNumero() { return this.numero; }
    public void setNumero(Integer numero) { this.numero = numero;}

    public String getEstado() { return this.estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public float getPrecio() { return this.precio; }
    public void setPrecio(float precio) { this.precio = precio; }

    public int getCantidadPersonas() { return this.cantidadPersonas; }
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
