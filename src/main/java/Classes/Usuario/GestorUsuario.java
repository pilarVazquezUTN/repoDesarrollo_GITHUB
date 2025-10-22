package Classes.Usuario;

import Classes.Usuario.UsuarioDAO;
import Classes.Usuario.UsuarioDTO;

public class GestorUsuario {
    public void autenticarUsuario(UsuarioDAO usuarioDAO, UsuarioDTO usuarioDTO){
        if(usuarioDAO.buscarUsuario(usuarioDTO)){
            System.out.println("encontrado \n");
        } else {
            System.out.println("noooooooo encontrado \n");
        }
    }
}
