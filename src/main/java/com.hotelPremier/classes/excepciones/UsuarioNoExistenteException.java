package Classes.Excepciones;

/**
 * Excepcion  para cuando el usuario ingresado no existe
 * en el sistema al intentar autenticarse.
 */
public class UsuarioNoExistenteException extends Exception {
    public UsuarioNoExistenteException(String mensaje) {
        super(mensaje);
    }
}
