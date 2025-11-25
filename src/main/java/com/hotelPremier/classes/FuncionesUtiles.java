package com.hotelPremier.classes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.hotelPremier.classes.estadia.EstadiaDTO;
import com.hotelPremier.classes.huesped.HuespedDTO;
import com.hotelPremier.classes.direccion.Direccion;
import com.hotelPremier.classes.direccion.DireccionDTO;
import com.hotelPremier.classes.estadia.Estadia;
import com.hotelPremier.classes.huesped.Huesped;
import com.hotelPremier.classes.reserva.Reserva;
import com.hotelPremier.classes.reserva.ReservaDTO;

//import com.hotelpremier.classes.estadia.EstadiaDTO;

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

    public  List<HuespedDTO> mapToDTOList(List<Huesped> listaHuespedes) {

        if (listaHuespedes == null || listaHuespedes.isEmpty()) {
            return new ArrayList<>();
        }

        // Usa Java Streams para un mapeo conciso y funcional:
        return listaHuespedes.stream()
                // Por cada 'entidad' en la lista, llama a 'mapToDTO'
                .map(this::mapToDTO)
                .collect(Collectors.toList()); // Recolecta en la lista final
    }


     /** Mapea una única Entidad Reserva a un único ReservaDTO.
     */
    public   HuespedDTO mapToDTO(Huesped huesped) {
        HuespedDTO huespedDTO = new HuespedDTO();
        DireccionDTO direccionDTO = new DireccionDTO();

        huespedDTO.setNombre(huesped.getNombre());
        huespedDTO.setApellido(huesped.getApellido());
        huespedDTO.setDni(huesped.getDni());
        huespedDTO.setTipoDocumento(huesped.getTipoDocumento());
        huespedDTO.setFechaNacimiento(huesped.getFechaNacimiento());
        huespedDTO.setTelefono(huesped.getTelefono());
        huespedDTO.setEmail(huesped.getEmail());
        
        direccionDTO.setID(huesped.getDireccionID());
        direccionDTO.setCalle(huesped.getDireccionCalle());
        direccionDTO.setNumero(huesped.getDireccionNumero());
        direccionDTO.setLocalidad(huesped.getDireccionLocalidad());
        direccionDTO.setDepartamento(huesped.getDireccionDepto());
        direccionDTO.setPiso(huesped.getDireccionPiso());
        direccionDTO.setCodigoPostal(huesped.getDireccionCodigoPostal());
        direccionDTO.setProvincia(huesped.getDireccionProvincia());
        direccionDTO.setPais(huesped.getDireccionPais());
        huespedDTO.setDireccionHuesped(direccionDTO);

        huespedDTO.setCuit(huesped.getCuit());
        huespedDTO.setPosicionIva(huesped.getPosicionIva());
        huespedDTO.setOcupacion(huesped.getOcupacion());
        huespedDTO.setNacionalidad(huesped.getNacionalidad());

        //recibo un huesped q tiene una lista de estadias, obtengo la lista y hago un for por las estadias
        //defino una list de estadias

        List<Estadia> listaEstadias = new ArrayList<>();
        listaEstadias=huesped.getListaEstadia();

        List<EstadiaDTO> listaEstadiaHuespedDTO = new ArrayList<>();

       //ahora recorro cada estadia, obtengo los datos y lo paso listaDTO
        for(Estadia estadialist : listaEstadias){
            EstadiaDTO estadiaDTO = new EstadiaDTO();
            estadiaDTO.setCheckin(estadialist.getCheckin());
            estadiaDTO.setCheckout(estadialist.getCheckout());

            listaEstadiaHuespedDTO.add(estadiaDTO);

        }
        huespedDTO.setListaEstadia(listaEstadiaHuespedDTO);
        return huespedDTO;
    }












}
