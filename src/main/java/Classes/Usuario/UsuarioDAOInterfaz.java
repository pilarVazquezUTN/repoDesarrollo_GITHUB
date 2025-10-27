package Classes.Usuario;

import Classes.Usuario.UsuarioDTO;

public interface UsuarioDAOInterfaz {
    boolean buscarUsuario(UsuarioDTO usuarioDTO);
    boolean usuarioEncontrado(String nombreUsuario, String contrasenaUsuario);
}
