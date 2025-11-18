package Classes.Reserva;

import Classes.DAOFactory;
import Classes.Huesped.HuespedDAO;

import java.util.ArrayList;
import java.util.List;

public class GestorReservaImplem implements GestorReservaInterfaz{
String id_empleado;
    ReservaDAO reservaDAO = (ReservaDAO) DAOFactory.create(DAOFactory.RESERVA);



    public void setIdEmpleado(String idEmpleado) {
    id_empleado = idEmpleado;
    }

    /**
     * Devuelve el id del empleado asociado.
     *
     * @return id del empleado
     */
    public String getIdEmpleado() {
  return  id_empleado;
    }

    /**
     * Registra una nueva reserva.
     */
    public void reservarHabitacion() {

    }

    /**
     * Cancela una reserva existente.
     */
    public void cancelarReserva() {

    }

    /**
     * Marca el ingreso de una reserva al sistema.
     */
    public void ingresaReserva() {

    }

    /**
     * Realiza el proceso de check-out de una reserva.
     */
    public void realizarCheckOut() {

    }
    public void realizarCheckIn() {

    }


    /**
     * busca reservas con el apellido pasado por parametro
     * @param apellido
     * @return
     */
    public List<ReservaDTO> reservasHuesped(String apellido) {
        List<ReservaDTO> listaReservas = new ArrayList<>();
        listaReservas= reservaDAO.read(apellido);
        return  listaReservas;
    }
}








