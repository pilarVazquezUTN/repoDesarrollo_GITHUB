package classes.reserva;
import java.util.Date;


public class ReservaDTO {
    private Date fechaDesde;
    private Date fechaHasta;
    private String estado;
    private String nombre;
    private String apellido;
    private int telefono;
    private int numeroHab;
    private String tipoHab;

    public void setNumeroHab(int numeroHab) {
        this.numeroHab = numeroHab;
    }
    public int getNumeroHab() {
        return numeroHab;
    }

    public void setTipoHab(String tipoHab) {
        this.tipoHab = tipoHab;

    }

    public String getTipoHab() {
        return tipoHab;
    }


    /**
     *
     * @param telefono
     */
    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    /**
     *
     * @return
     */
    public int getTelefono() {
        return telefono;
    }

    /**
     *
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     *
     * @return
     */
    public String getNombre() {
        return nombre;
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
     * @return
     */
    public String getApellido() {
        return apellido;
    }

    /**
     *
     * @param estado
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     *
     * @return
     */
    public String getEstado() {
        return estado;
    }

    /**
     *
     * @return
     */
    public Date getFechaDesde() {
        return fechaDesde;
    }

    /**
     *
     * @param fechaDesde
     */
    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    /**
     *
     * @return
     */
    public Date getFechaHasta() {
        return fechaHasta;
    }

    /**
     *
     * @param fechaHasta
     */
    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }
}
