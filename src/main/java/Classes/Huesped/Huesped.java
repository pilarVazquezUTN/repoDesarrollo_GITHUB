package Classes.Huesped;

import java.util.Calendar;
import java.util.Date;

import Classes.Direccion.DireccionDTO;

public class Huesped {
    private String apellido;
    private String nombre;
    private String tipoDocumento;
    private String numeroDocumento;
    private Date fechaNacimiento;
    private String telefono;
    private String email;
    private DireccionDTO direccionHuesped;
    private String cuit;
    private String posicionIva;
    private String ocupacion;
    private String nacionalidad;

    
    public boolean esMayorDeEdad(HuespedDTO huespedDTO) {
        Date fechaNacimiento = huespedDTO.getFechaNacimiento();

        Calendar hoy = Calendar.getInstance();
        Calendar nacimiento = Calendar.getInstance();
        nacimiento.setTime(fechaNacimiento);

        int edad = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR);

        // Si todavía no cumplió , restamos 1 porq todavia no complio años pero en la resta da 18
        if (hoy.get(Calendar.MONTH) < nacimiento.get(Calendar.MONTH) ||
                ((hoy.get(Calendar.MONTH) == nacimiento.get(Calendar.MONTH) &&
                        hoy.get(Calendar.DAY_OF_MONTH) < nacimiento.get(Calendar.DAY_OF_MONTH)))){
            edad--;
        }

        return edad >= 18;
    }
    
}


