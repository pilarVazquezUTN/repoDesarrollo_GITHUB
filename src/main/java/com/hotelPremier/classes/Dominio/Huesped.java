package com.hotelPremier.classes.Dominio;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hotelPremier.classes.Dominio.responsablePago.ResponsablePago;

import jakarta.persistence.*;

@Entity
@Table(name = "huesped")
public class Huesped {

    @Column(name="apellido")
    private String apellido;

    @Column(name="nombre")
    private String nombre;

    @EmbeddedId
    private HuespedID huespedID;

    @Temporal(TemporalType.DATE)
    @Column(name="fechanacimiento")
    private Date fechaNacimiento;

    @Column(name="telefono")
    private String telefono;

    @Column(name="email")
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_direccion")
    private Direccion direccion;

    @Column(name="cuit")
    private String cuit;

    @Column(name="posicioniva")
    private String posicionIva;

    @Column(name="ocupacion")
    private String ocupacion;

    @Column(name="nacionalidad")
    private String nacionalidad;


 @ManyToMany
@JoinTable(
    name = "relac_estadia_huesped",
    joinColumns = {
        @JoinColumn(name = "dni", referencedColumnName = "dni"),
        @JoinColumn(name = "tipodocumento", referencedColumnName = "tipodocumento")
    },
    inverseJoinColumns = @JoinColumn(name = "id_estadia")
)
@JsonBackReference(value = "estadia-huespedes")
private List<Estadia> listaEstadia;


    @OneToOne
    @JoinColumn(name = "id_responsablepago")
    private ResponsablePago responsablePago;

    public Huesped() {
        this.huespedID = new HuespedID();
    }

    // ============================
    // GETTERS & SETTERS
    // ============================

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public Date getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(Date fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public HuespedID getHuespedID() { return huespedID; }
    public void setHuespedID(HuespedID huespedID) { this.huespedID = huespedID; }

    public Direccion getDireccion() { return direccion; }
    public void setDireccion(Direccion direccion) { this.direccion = direccion; }

    public String getCuit() { return cuit; }
    public void setCuit(String cuit) { this.cuit = cuit; }

    public String getPosicionIva() { return posicionIva; }
    public void setPosicionIva(String posicionIva) { this.posicionIva = posicionIva; }

    public String getOcupacion() { return ocupacion; }
    public void setOcupacion(String ocupacion) { this.ocupacion = ocupacion; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    public List<Estadia> getListaEstadia() { return listaEstadia; }
    public void setListaEstadia(List<Estadia> listaEstadia) { this.listaEstadia = listaEstadia; }

    public ResponsablePago getResponsablePago() { return responsablePago; }
    public void setResponsablePago(ResponsablePago responsablePago) { this.responsablePago = responsablePago; }
}
