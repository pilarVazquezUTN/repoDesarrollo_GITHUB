package Classes.Usuario;

import Classes.Excepciones.UsuarioNoExistenteException;
import Classes.Usuario.UsuarioDTO;

public interface UsuarioDAOInterfaz {
    boolean buscarUsuario(UsuarioDTO usuarioDTO)  throws UsuarioNoExistenteException;
    boolean usuarioEncontrado(String nombreUsuario, String contrasenaUsuario);
}
