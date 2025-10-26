package Classes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.Predicate;

public class Validador {

    /**
     * fecha valida
     */
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static Predicate<String> esFechaValida = fecha -> {
        try {
            LocalDate.parse(fecha, formatter);
            return true;  // fecha válida
        } catch (DateTimeParseException e) {
            return false; // fecha inválida
        }
    };

    /**
     *predicado para chequear strings
     */
    public static Predicate<String> esStringValido =
            valor -> valor.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");
    /**
     * verifica email
     */
    public static Predicate<String> esMailValido = mail -> {
        if (mail == null || mail.isEmpty()) {
            return true; // Consideramos null o vacío como válido
        }
        return mail.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    };

    public static Predicate<String> esCalleValida = calle -> {
        if (calle == null || calle.isEmpty()) {
            return true; // null o vacío también válido
        }
        return calle.matches("^[a-zA-Z0-9 ]+$");
    };
    /**
     * chequea solo numeros
     */
    public static Predicate<String> esNumeroValido =
            valor -> valor != null && valor.matches("\\d+");

    /**
     *
     * @param valor : es el numero de cuit
     * @return : retorna true si es valido o no seun como se valida por afip
     */
    public static boolean esCuitValido(String valor) {

        if (valor == null || valor.isEmpty()) return true;

        //Quitar guiones si los tiene
        String cuit = valor.replaceAll("-", "");

        // longitud y solo num
        if (!cuit.matches("\\d{11}")) return false;

        //  dígito verificador
        int[] pesos = {5, 4, 3, 2, 7, 6, 5, 4, 3, 2};
        int suma = 0;

        for (int i = 0; i < 10; i++) {
            suma += Character.getNumericValue(cuit.charAt(i)) * pesos[i];
        }

        int resto = suma % 11;
        int verificador = 11 - resto;
        if (verificador == 11) verificador = 0;
        else if (verificador == 10) verificador = 9;

        // Comparar con el último dígito
        int ultimo = Character.getNumericValue(cuit.charAt(10));
        return verificador == ultimo;
    }





}
