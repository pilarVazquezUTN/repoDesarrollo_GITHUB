package Classes.Usuario;

import Classes.Usuario.UsuarioDAO;
import Classes.Usuario.UsuarioDTO;



public class GestorUsuario {
    public boolean autenticarUsuario(UsuarioDAO usuarioDAO, UsuarioDTO usuarioDTO){
        boolean encontrado=false;
        if(usuarioDAO.buscarUsuario(usuarioDTO)){
            encontrado=true;
        }
        return encontrado;
    }
}
