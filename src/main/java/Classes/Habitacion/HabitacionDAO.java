package Classes.Habitacion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import Classes.FuncionesUtiles;

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
        abrirArchivoCsvHabitaciones(desdeFecha,hastaFecha);    
    } 
    public void abrirArchivoCsvHabitaciones(Date desdeFecha, Date hastaFecha){
        String FECHA_FORMATO = "dd/MM/yyyy";
        String NOMBRE_ARCHIVO = "infoHabitaciones.csv";
        String SEPARADOR_CSV = ",";
        SimpleDateFormat formatter = new SimpleDateFormat(FECHA_FORMATO);
        formatter.setLenient(false);
        FuncionesUtiles funcionesUtiles = new FuncionesUtiles();

        try (BufferedReader br = new BufferedReader(new FileReader(NOMBRE_ARCHIVO))) {
            String linea;
            boolean existeHabitacion=false;            
            // Opcional: Saltar la línea de encabezado si existe
            // br.readLine(); 

            // Leer línea por línea
            while ((linea = br.readLine()) != null) {
                // Dividir la línea por el separador (ej: coma)
                String[] campos = linea.split(SEPARADOR_CSV);
                
                if (campos.length >= 7) { 
                    String numHabitacion = campos[0].trim();
                    String estadoHabitacion = campos[1].trim();
                    String precioHabitacion = campos[2].trim();
                    String capacidadHabitacion = campos[3].trim();
                    String estadoReserva = campos[4].trim();
                    String desdeFechaHabitacioString=campos[5].trim();
                    Date desdeFechaHabitacion = funcionesUtiles.convertirStringADate(desdeFechaHabitacioString);
                    String hastaFechaHabitacionString=campos[6].trim();
                    Date hastaFechaHabitacion = funcionesUtiles.convertirStringADate(hastaFechaHabitacionString);

                    if (desdeFechaHabitacion != null && hastaFechaHabitacion != null) {
                        if(desdeFechaHabitacion.before(desdeFecha) && hastaFecha.before(hastaFechaHabitacion) && !"ocupada".equals(estadoHabitacion)){
                        System.out.println("Habitacion nro: "+numHabitacion+"\n con estado: "+estadoHabitacion+"\n con precio: "+precioHabitacion+"\n capacidad: "+capacidadHabitacion+"\n estado de la reserva: "+estadoReserva+"\n desde fecha: "+funcionesUtiles.convertirDateAString(desdeFechaHabitacion)+"\n hasta fecha: "+funcionesUtiles.convertirDateAString(hastaFechaHabitacion)+"\n");
                        existeHabitacion=true;
                        }
                        //System.out.println("Habitacion nro: "+numHabitacion+"\n con estado: "+estadoHabitacion+"\n con precio: "+precioHabitacion+"\n capacidad: "+capacidadHabitacion+"\n estado de la reserva: "+estadoReserva+"\n desde fecha: "+desdeFechaHabitacion+"\n hasta fecha: "+hastaFechaHabitacion+"\n");
                    }
                    
                } else {
                    System.err.println("Advertencia: Línea incompleta o con formato incorrecto: " + linea);
                }
            }
            if(!existeHabitacion){
                System.out.println("No existen habitaciones disponibles para las fechas solicitadas. ");
            }

        } catch (IOException e) {
            System.err.println("Error al leer el archivo " + NOMBRE_ARCHIVO + ": " + e.getMessage());
        }
    }
}
