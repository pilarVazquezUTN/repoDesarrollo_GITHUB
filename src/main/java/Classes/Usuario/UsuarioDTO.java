package Classes.Usuario;

public class UsuarioDTO {
    String nombre;
    String contraseña;

        
    public void setNombre(String unNombre){
        this.nombre= unNombre;
    }
    public void setContraseña(String unaContraseña){
        this.contraseña= unaContraseña;
    }

    public void getNombre(){
        return this.nombre;
    }
    public void getContraseña(){
        return this.contraseña;
    }
}
