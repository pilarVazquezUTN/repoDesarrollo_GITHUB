package classes.usuario;

import classes.Excepciones.UsuarioNoExistenteException;
import classes.usuario.usuarioDTO;

public interface UsuarioDAOInterfaz {
    boolean buscarUsuario(UsuarioDTO usuarioDTO)  throws UsuarioNoExistenteException;
    boolean usuarioEncontrado(String nombreUsuario, String contrasenaUsuario);
}
