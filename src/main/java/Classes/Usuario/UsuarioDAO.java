package Classes.Usuario;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class UsuarioDAO {
    
    public boolean buscarUsuario(UsuarioDTO usuarioDTO){
        // 1. CORRECCIÓN: Llamar a los métodos getter con paréntesis ()
        // 2. CORRECCIÓN: Asumir que el DTO usa getContrasena (sin ñ)
        if(usuarioEncontrado(usuarioDTO.getNombre(), usuarioDTO.getContrasena())){
            return true; // Retorna true inmediatamente si se encuentra
        }
        return false;
    }

    public boolean usuarioEncontrado(String nombreUsuario, String contrasenaUsuario) {
        String usuarioIngresado = nombreUsuario;
        
        // 3. CORRECCIÓN: La declaración de la variable estaba mal escrita (espacio)
        String contrasenaIngresada = contrasenaUsuario; 
        
        String rutaArchivo = "D:/UTN/2025/DesarrolloSoftware/trabajoPractico/infoAutenticarUsuario.txt";
        boolean autenticado = false;

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Separar usuario y contraseña por coma
                String[] partes = linea.split(",");
                if (partes.length == 2) {
                    String usuario = partes[0].trim();
                    String contrasena = partes[1].trim();

                    // Comparar ambos (ignorando mayúsculas en el usuario)
                    if (usuario.equalsIgnoreCase(usuarioIngresado) &&
                        contrasena.equals(contrasenaIngresada)) {
                        autenticado = true;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            // Si hay un error de IO, se considera que el usuario no fue encontrado (autenticado = false)
        }

        return autenticado;
    }
}