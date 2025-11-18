package Classes.Reserva;

import java.util.List;

/**
 * Clase que gestiona las acciones relacionadas con las reservas.
 * Permite asignar, cancelar e ingresar reservas, y manejar el check-out.
 */
public interface GestorReservaInterfaz {

    /**
     * Asigna el id del empleado que realiza la operacion.
     *
     * @param idEmpleado identificador del empleado
     */
    public void setIdEmpleado(String idEmpleado) ;





    /**
     * Devuelve el id del empleado asociado.
     *
     * @return id del empleado
     */

    public String getIdEmpleado() ;
    /**
     * Registra una nueva reserva.
     */
    public void reservarHabitacion() ;

    /**
     * Cancela una reserva existente.
     */
    public void cancelarReserva() ;

    /**
     * Marca el ingreso de una reserva al sistema.
     */
    public void ingresaReserva() ;

    /**
     * Realiza el proceso de check-out de una reserva.
     */
    public void realizarCheckOut() ;

    public void realizarCheckIn() ;

    public List<ReservaDTO> reservasHuesped(String apellido) ;  //metodo que devulve todas las reservas en base a un apellido



}
