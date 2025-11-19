package com.hotelPremier.classes;


import com.hotelPremier.classes.direccion.DireccionDAO;
import com.hotelPremier.classes.estadia.EstadiaDAO;
import com.hotelPremier.classes.factura.FacturaDAO;
import com.hotelPremier.classes.factura.NotaDeCreditoDAO;
import com.hotelPremier.classes.habitacion.HabitacionDAO;
import com.hotelPremier.classes.huesped.HuespedDAO;
import com.hotelPremier.classes.medioDePago.MedioDePagoDAO;
import com.hotelPremier.classes.pago.PagoDAO;
import com.hotelPremier.classes.responsablePago.PersonaJuridicaDAO;
import com.hotelPremier.classes.usuario.UsuarioDAO;

/**
 * Es una clase que implementa el patrón Factory.
 * Su función es centralizar la creación de instancias de objetos DAO
 * 
 * Permite separar la lógica de negocio de la lógica de creacion de objetos ,
 * delegando la creación de los objetos DAO en una única clase responsable.
 * 
 * 
 * Ejemplo de uso:
 * 
 * <pre>{@code
 * HuespedDAO dao = (HuespedDAO) DAOFactory.create(DAOFactory.HUESPED);
 * }</pre>
 */
public class DAOFactory {
    public static final int DIRECCION = 0;
    public static final int ESTADIA = 1;
    public static final int FACTURA = 2;
    public static final int NOTA_DE_CREDITO = 3;
    public static final int HABITACION = 4;
    public static final int HUESPED = 5;
    public static final int MEDIO_DE_PAGO = 6;
    public static final int PAGO = 7;
    public static final int RESERVA = 8;
    public static final int PERSONA_JURIDICA = 9;
    public static final int USUARIO = 10;
    
    
    public static int[] tipos = {
        DIRECCION, ESTADIA, FACTURA, NOTA_DE_CREDITO, 
        HABITACION, HUESPED, MEDIO_DE_PAGO, PAGO, 
        RESERVA, PERSONA_JURIDICA, USUARIO
    };
    /**
     * Crea una instancia de un objeto DAO dependiendo del tipo de objeto que se le pase.
     * @param tipo El tipo de objeto DAO que se desea crear.
     * @return Una instancia de un objeto DAO.
     */
    public static Object create(int tipo) {
        switch(tipo) {
            case DIRECCION:
                return DireccionDAO.getInstancia();
            case ESTADIA:
                return EstadiaDAO.getInstancia();
            case FACTURA:
                return FacturaDAO.getInstancia();
            case NOTA_DE_CREDITO:
                return NotaDeCreditoDAO.getInstancia();
            case HABITACION:
                return HabitacionDAO.getInstancia();
            case HUESPED:
                return HuespedDAO.getInstancia();
            case MEDIO_DE_PAGO:
                return MedioDePagoDAO.getInstancia();
            case PAGO:
                return PagoDAO.getInstancia();
            case RESERVA:
               // return ReservaDAO.getInstancia();
            case PERSONA_JURIDICA:
                return PersonaJuridicaDAO.getInstancia();
            case USUARIO:
                return UsuarioDAO.getInstancia();
            default:
                return null;
        }
    }
}
