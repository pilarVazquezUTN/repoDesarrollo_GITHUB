package com.hotelPremier.classes.usuario;

import com.hotelPremier.classes.excepciones.UsuarioNoExistenteException;

public class GestorUsuario {

    /**
     * Autentica un usuario en el sistema verificando si existe y si la contraseña es correcta.
     *
     * Si el usuario no existe o la contraseña es incorrecta, se captura la excepcion
     * UsuarioNoExistenteException y se informa al usuario por consola.
     *
     * @param usuarioDAO instancia del DAO que realiza la verificacion de datos
     * @param usuarioDTO objeto con los datos de autenticacion del usuario
     * @return true si el usuario fue autenticado correctamente, false si no existe o hubo error
     */
    public boolean autenticarUsuario(UsuarioDAO usuarioDAO, UsuarioDTO usuarioDTO) {
        boolean encontrado = false;

        try {
            if (usuarioDAO.buscarUsuario(usuarioDTO)) {
                encontrado = true;
            }
        } catch (UsuarioNoExistenteException e) {
            System.out.println(e.getMessage());
        }

        return encontrado;
    }
}
