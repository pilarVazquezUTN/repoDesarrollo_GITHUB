package Classes;

import Classes.Direccion.DireccionDAO;
import Classes.Estadia.EstadiaDAO;
import Classes.Factura.FacturaDAO;
import Classes.Factura.NotaDeCreditoDAO;
import Classes.Habitacion.HabitacionDAO;
import Classes.Huesped.HuespedDAO;
import Classes.MedioDePago.MedioDePagoDAO;
import Classes.Pago.PagoDAO;
import Classes.Reserva.ReservaDAO;
import Classes.ResponsablePago.PersonaJuridicaDAO;
import Classes.Usuario.UsuarioDAO;

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

    public static Object create(int tipo) {
        switch(tipo) {
            case DIRECCION:
                return new DireccionDAO();
            case ESTADIA:
                return new EstadiaDAO();
            case FACTURA:
                return new FacturaDAO();
            case NOTA_DE_CREDITO:
                return new NotaDeCreditoDAO();
            case HABITACION:
                return new HabitacionDAO();
            case HUESPED:
                return new HuespedDAO();
            case MEDIO_DE_PAGO:
                return new MedioDePagoDAO();
            case PAGO:
                return new PagoDAO();
            case RESERVA:
                return new ReservaDAO();
            case PERSONA_JURIDICA:
                return new PersonaJuridicaDAO();
            case USUARIO:
                return new UsuarioDAO();
            default:
                return null;
        }
    }
}
