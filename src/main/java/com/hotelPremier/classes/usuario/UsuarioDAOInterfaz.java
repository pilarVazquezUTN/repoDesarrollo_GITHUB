package com.hotelPremier.classes.usuario;

import com.hotelPremier.classes.excepciones.UsuarioNoExistenteException;
import com.hotelPremier.classes.usuario.UsuarioDTO;

public interface UsuarioDAOInterfaz {
    boolean buscarUsuario(UsuarioDTO usuarioDTO)  throws UsuarioNoExistenteException;
    boolean usuarioEncontrado(String nombreUsuario, String contrasenaUsuario);
}
