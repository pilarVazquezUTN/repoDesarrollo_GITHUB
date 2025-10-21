package Classes.Usuario;

public class UsuarioDAO {
    public boolean buscarUsuario(UsuarioDTO usuarioDTO){
        boolean encontrado=false;
    
        if(usuarioEncontado(usuarioDTO.getNombre,usuarioDTO.getContraseña)){
            encontrado=true;
        }

        return encontrado;
    }

    public boolean usuarioEncontrado(String nombreUsuario, String contraseñaUsuario) {
        String usuarioIngresado = nombreUsuario;
        String contrasenaIngresada = contraseñaUsuario;
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
        }

        if (autenticado) {
            encontrado=true;
        } 

        return encontrado;
    }
}
