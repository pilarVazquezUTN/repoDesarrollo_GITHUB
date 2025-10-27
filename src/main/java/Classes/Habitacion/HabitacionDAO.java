package Classes.Habitacion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class HabitacionDAO implements HabitacionDAOInterfaz {
    public void delete(){

    }
    public  void create(){

    } 
    public  void update(){

    }
    public  void read(){
        
    }
    public void muestraEstado(Date desdeFecha, Date hastaFecha){
        /*
        map: key:numero de habitacion, value:Fecha(ir sacando desdeFecha y hastaFecha)

         */
        //System.out.println("aca mostrariamos la grilla de habitaciones con sus estados.");
        /*
        funcion para abrir csv segun desdeFecha y hastaFecha
         */
        abrirArchivoCsvHabitaciones(desdeFecha,hastaFecha);    
    } 
    public void abrirArchivoCsvHabitaciones(Date desdeFecha, Date hastaFecha){
        String FECHA_FORMATO = "dd/MM/yyyy";
        String NOMBRE_ARCHIVO = "infoHabitaciones.csv";
        String SEPARADOR_CSV = ";";
        SimpleDateFormat formatter = new SimpleDateFormat(FECHA_FORMATO);
        formatter.setLenient(false);

        try (BufferedReader br = new BufferedReader(new FileReader(NOMBRE_ARCHIVO))) {
            String linea;
            
            // Opcional: Saltar la línea de encabezado si existe
            // br.readLine(); 

            // Leer línea por línea
            while ((linea = br.readLine()) != null) {
                
                // Dividir la línea por el separador (ej: coma)
                String[] campos = linea.split(SEPARADOR_CSV);
                
                // Asumimos que desdeFecha es el campo[0] y hastaFecha es el campo[1]
                if (campos.length >= 7) { 
                    String numHabitacion = campos[0].trim();
                    String estadoHabitacion = campos[1].trim();
                    String precioHabitacion = campos[2].trim();
                    String capacidadHabitacion = campos[3].trim();
                    String estadoReserva = campos[4].trim();
                    String desdeFechaString = campos[5].trim();
                    String hastaFechaString = campos[6].trim();

                    System.out.println("Habitacion nro: "+numHabitacion+"\n con estado: "+estadoHabitacion+"\n con precio: "+precioHabitacion+"\n capacidad: "+capacidadHabitacion+"\n estado de la reserva: "+estadoReserva+"\n desde fecha: "+desdeFechaString+"\n hasta fecha: "+hastaFechaString+"\n");
                } else {
                    System.err.println("Advertencia: Línea incompleta o con formato incorrecto: " + linea);
                }
            }

        } catch (IOException e) {
            System.err.println("Error al leer el archivo " + NOMBRE_ARCHIVO + ": " + e.getMessage());
        }
    }
}
