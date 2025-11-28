package com.hotelPremier.classes.huesped;
//import Classes.Estadia.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.hotelPremier.classes.direccion.Direccion;
import com.hotelPremier.classes.direccion.DireccionDTO;
import com.hotelPremier.classes.estadia.Estadia;
import com.hotelPremier.classes.estadia.EstadiaDTO;
import com.hotelPremier.classes.reserva.Reserva;
import com.hotelPremier.classes.responsablePago.ResponsablePago;
import jakarta.persistence.*; // Usa jakarta.persistence para Spring Boot 3+
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;//!!!!!

@JsonIgnoreProperties("listaestadia")//!!!!!!
@Entity
@Table(name = "huesped") // 2. (Opcional) Especifica el nombre de la tabla
public class Huesped { 

    @Column(name="apellido")
    private String apellido;
    @Column(name="nombre")
    private String nombre;
    @EmbeddedId
    HuespedID huespedID;


    @Temporal(TemporalType.DATE) //guarda fecha sin hora
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "relac_estadia_huesped",
            joinColumns = {
                @JoinColumn(name = "tipodocumento", referencedColumnName = "tipodocumento"),
                @JoinColumn(name = "dni", referencedColumnName = "dni")
            },
            inverseJoinColumns = @JoinColumn(name = "id_estadia")
    )
    private List<Estadia> listaestadia;


    @OneToOne
    @JoinColumn(name = "id_responsablepago")
    private ResponsablePago responsablePago;



    public Huesped() {
        this.huespedID = new HuespedID();
    }


    public String getDni() {
        return this.huespedID.getDni();
    }

    public String getTipoDocumento() {
        return this.huespedID.getTipoDocumento();
    }

    // Setters del ID compuesto
    public void setDni(String dni) {
        if (this.huespedID == null) this.huespedID = new HuespedID();
        this.huespedID.setDni(dni);
    }

    public void setTipoDocumento(String tipoDocumento) {
        if (this.huespedID == null) this.huespedID = new HuespedID();
        this.huespedID.setTipoDocumento(tipoDocumento);
    }
    public HuespedID getHuespedID(){
        return this.huespedID;
    }
    public void setHuespedID(HuespedID id){
        this.huespedID.setDni(id.getDni());
        this.huespedID.setTipoDocumento(id.getTipoDocumento());
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     *
     * @param apellido
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     *
     * @param tipoDocumento
     */



    /**
     *
     * @param fechaNacimiento
     */
    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     *
     * @param telefono
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }


    /**
     *
     * @param cuit
     */
    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    /**
     *
     * @param posicionIva
     */
    public void setPosicionIva(String posicionIva) {
        this.posicionIva = posicionIva;
    }

    /**
     *
     * @param ocupacion
     */
    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    /**
     *
     * @param nacionalidad
     */
    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public void setDireccion(Direccion direc){
        this.direccion=direc;
    }

    public Direccion getDireccion(){
        return this.direccion;
    }
    
    /**
     *
     * @return
     */
    public Date getFechaNacimiento() {
        return this.fechaNacimiento;
    }

    /**
     *
     * @return
     */
    public String getApellido() {
        return this.apellido;
    }

    /**
     *
     * @return
     */

    /**
     *
     * @return
     */

    /**
     *
     * @return
     */
    public String getCuit() {
        return this.cuit;
    }

    /**
     *
     * @return
     */
    public String getPosicionIva() {
        return this.posicionIva;
    }

    /**
     *
     * @return
     */
    public String getOcupacion() {
        return this.ocupacion;
    }

    /**
     *
     * @return
     */
    public String getNacionalidad() {
        return this.nacionalidad;
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return this.email;
    }

    /**
     *
     * @return
 */

    /**
     *
     * @return
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     *
     * @return
     */
    public String getTelefono() {
        return this.telefono;
    }


    public List<Estadia> getListaEstadia(){
        return this.listaestadia;
    }
    public void setListaEstadia(List<Estadia> lista){
        this.listaestadia=lista;
    }

}


