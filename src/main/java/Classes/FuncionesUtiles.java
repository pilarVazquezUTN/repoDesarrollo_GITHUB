package Classes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FuncionesUtiles {
    public Date convertirStringADate(String fechaString) {
        
        // 1. Define el formato EXACTO del String de entrada. 
        // ¡Importante usar 'MM' para mes y 'yyyy' para año!
        String formatoEntrada = "dd/MM/yyyy";
        
        SimpleDateFormat formateador = new SimpleDateFormat(formatoEntrada);
        
        // Hace que la validación sea estricta (no acepta fechas como 32/01/2025)
        formateador.setLenient(false); 
        
        try {
            // 2. Utiliza el método parse() para realizar la conversión.
            Date fechaConvertida = formateador.parse(fechaString);
            return fechaConvertida;
            
        } catch (ParseException e) {
            // 3. Maneja el error si la cadena no coincide con el formato.
            System.err.println("Error: Formato de fecha inválido. Se esperaba: " + formatoEntrada);
            return null; // Devuelve null si la conversión falla.
        }
    }


    public static void clearConsola() {
            System.out.print("\033[H\033[2J");
            System.out.flush();
    }

    public static boolean esUnSoloNumero(String valor) {
        return valor.matches("[0-9]"); // solo un dígito entre 0 y 9
    }


    public static String convertirDateAString(Date fechaDate) {
        // 1. Define el formato de salida deseado.
        String formatoSalida = "dd/MM/yyyy"; 
        
        // 2. Crea el formateador.
        SimpleDateFormat formateador = new SimpleDateFormat(formatoSalida);
        
        // 3. Usa el método format() para realizar la conversión.
        return formateador.format(fechaDate);
    }


    public static boolean contieneSoloLetras(String palabra){
        boolean soloLetras=true;
        if(palabra==null || palabra.isEmpty()){
            soloLetras=false;
        }
        String regex = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$";

        return palabra.matches(regex);
    }


    public static boolean contieneSoloNumeros(String palabra){
        boolean soloLetras=true;
        if(palabra==null || palabra.isEmpty()){
            soloLetras=false;
        }
        String regex = "^\\d+$";

        return palabra.matches(regex);
    }


    public static boolean emailValido(String email){
        boolean soloLetras=true;
        if(email==null){
            soloLetras=false;
        }
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

        return email.matches(regex);
    }
}
