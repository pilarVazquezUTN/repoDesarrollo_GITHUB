package Classes.Excepciones;

/**
 * Excepcion personalizada que indica que no se encontro ningun huesped
 * que coincida con los criterios de busqueda.
 */
public class HuespedNoEncontradoException extends Exception {
    public HuespedNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}

