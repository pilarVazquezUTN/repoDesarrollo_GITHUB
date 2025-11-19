package com.hotelPremier.classes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FuncionesUtiles {


    /**
     * convierte el string a Date
     * @param fechaString
     * @return
     */
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

    /**
     * valida q solo sea un numero lo q ingresa en string
     * @param valor
     * @return
     */
    public static boolean esUnSoloNumero(String valor) {
        return valor.matches("[0-9]"); // solo un dígito entre 0 y 9
    }

    /**
     * convierte el tipo DATE a string
     * @param fechaDate
     * @return
     */
    public static String convertirDateAString(Date fechaDate) {
        // 1. Define el formato de salida deseado.
        String formatoSalida = "dd/MM/yyyy"; 
        
        // 2. Crea el formateador.
        SimpleDateFormat formateador = new SimpleDateFormat(formatoSalida);
        
        // 3. Usa el método format() para realizar la conversión.
        return formateador.format(fechaDate);
    }

    /**
     * valida q solo haya letras
     * @param palabra
     * @return
     */
    public static boolean contieneSoloLetras(String palabra){
        boolean soloLetras=true;
        if(palabra==null || palabra.isEmpty()){
            soloLetras=false;
        }
        String regex = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$";

        return palabra.matches(regex);
    }

    /**
     * solo numeros
     * @param palabra
     * @return
     */
    public static boolean contieneSoloNumeros(String palabra){
        boolean soloLetras=true;
        if(palabra==null || palabra.isEmpty()){
            soloLetras=false;
        }
        String regex = "^\\d+$";

        return palabra.matches(regex);
    }

    /**
     * valida q sea mail valido
     * @param email
     * @return
     */
    public static boolean emailValido(String email){
        boolean soloLetras=true;
        if(email==null){
            soloLetras=false;
        }
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

        return email.matches(regex);
    }

    public static boolean tipoDocumentoValido(String tipoDoc){
        boolean tipoDocValido=false;

        if(tipoDoc.equals("DNI") || tipoDoc.equals("LE") || tipoDoc.equals("LC") || tipoDoc.equals("PASAPORTE")){
            tipoDocValido=true;
        }
        return tipoDocValido;
    }
}
