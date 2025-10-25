package classes.usuario;

import classes.usuario.UsuarioDAO;
import classes.usuario.UsuarioDTO;



public class GestorUsuario {
    public boolean autenticarUsuario(UsuarioDAO usuarioDAO, UsuarioDTO usuarioDTO){
        boolean encontrado=false;
        if(usuarioDAO.buscarUsuario(usuarioDTO)){
            encontrado=true;
        }
        return encontrado;
    }
}
