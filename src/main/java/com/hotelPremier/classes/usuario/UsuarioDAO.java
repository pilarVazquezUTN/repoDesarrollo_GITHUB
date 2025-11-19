package com.hotelPremier.classes.usuario;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.hotelPremier.classes.excepciones.UsuarioNoExistenteException;

public class UsuarioDAO implements UsuarioDAOInterfaz {
    private static UsuarioDAO instancia; // unica instancia

    private UsuarioDAO() { }

    /**
     * Devuelve la única instancia de UsuarioDAO.
     * Si todavía no fue creada, la instancia se genera y se guarda.
     *
     * @return instancia única de UsuarioDAO
     */
    public static synchronized UsuarioDAO getInstancia() {
        if (instancia == null) {
            instancia = new UsuarioDAO();
        }
        return instancia;
    }

    /**
     * Busca si el usuario ingresado existe y coincide la contraseña.
     *
     * @param usuarioDTO objeto con el nombre y la contraseña a validar
     * @return true si el usuario y contraseña coinciden, false en caso contrario
     * @throws UsuarioNoExistenteException si el usuario no se encuentra en el sistema
     */
    public boolean buscarUsuario(UsuarioDTO usuarioDTO) throws UsuarioNoExistenteException {
        if (usuarioEncontrado(usuarioDTO.getNombre(), usuarioDTO.getContrasena())) {
            return true;
        } else {
            throw new UsuarioNoExistenteException("El usuario '" + usuarioDTO.getNombre() + "' no existe o la contraseña es incorrecta.");
        }
    }

    /**
     * Verifica si el usuario y contraseña coinciden con los registros del archivo.
     *
     * @param nombreUsuario nombre del usuario a verificar
     * @param contrasenaUsuario contraseña ingresada
     * @return true si el usuario fue encontrado y autenticado, false en caso contrario
     */
    public boolean usuarioEncontrado(String nombreUsuario, String contrasenaUsuario) {
        String rutaArchivo = "infoAutenticarUsuario.txt";
        boolean autenticado = false;

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 2) {
                    String usuario = partes[0].trim();
                    String contrasena = partes[1].trim();

                    if (usuario.equalsIgnoreCase(nombreUsuario) &&
                        contrasena.equals(contrasenaUsuario)) {
                        autenticado = true;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }

        return autenticado;
    }
}
