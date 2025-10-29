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
    private static HabitacionDAO instancia; // única instancia

    private HabitacionDAO() { }

    public static synchronized HabitacionDAO getInstancia() {
        if (instancia == null) {
            instancia = new HabitacionDAO();
        }
        return instancia;
    }
    public void delete(){

    }
    public  void create(){

    } 
    public  void update(){

    }
    public  void read(){
        
    }
    public void muestraEstado(String tipoHabitacion, Date desdeFecha, Date hastaFecha){
        mostrarGrillaHabitaciones(tipoHabitacion,desdeFecha, hastaFecha);
        //abrirArchivoCsvHabitaciones(desdeFecha,hastaFecha);    
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
                        // && !"ocupada".equals(estadoHabitacion)
                        if(desdeFechaHabitacion.before(desdeFecha) && hastaFecha.before(hastaFechaHabitacion)){
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

    public void mostrarGrillaHabitaciones(String tipoHabitacion, Date desdeFecha, Date hastaFecha) {
    String FECHA_FORMATO = "dd/MM/yyyy";
    String NOMBRE_ARCHIVO = "infoHabitaciones.csv";
    String SEPARADOR_CSV = ",";
    SimpleDateFormat formatter = new SimpleDateFormat(FECHA_FORMATO);
    FuncionesUtiles funcionesUtiles = new FuncionesUtiles();

    // 1. Generar lista de fechas (columnas de la grilla)
    List<Date> listaFechas = new ArrayList<>();
    Calendar cal = Calendar.getInstance();
    cal.setTime(desdeFecha);

    while (!cal.getTime().after(hastaFecha)) {
        listaFechas.add(cal.getTime());
        cal.add(Calendar.DATE, 1);
    }

    // 2. Leer TODAS las líneas del CSV
    // Esta lista contendrá todas las reservas, ocupaciones, etc.
    List<String[]> habitacionesCSV = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(NOMBRE_ARCHIVO))) {
        br.readLine(); // Saltar encabezado
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] campos = linea.split(SEPARADOR_CSV, -1);
            if (campos.length >= 7) {
                habitacionesCSV.add(campos);
            } else {
                System.err.println("Línea inválida (faltan columnas): " + linea);
            }
        }
    } catch (IOException e) {
        System.err.println("Error al leer el archivo: " + e.getMessage());
        return;
    }

    // 3. Determinar rango de habitaciones (filas de la grilla)
    int rangoMin = 0;
    int rangoMax = 0;

    // Usamos .toLowerCase() para que coincida con los case
    switch (tipoHabitacion) {
        case "Individual Estandar":
            rangoMin = 1;
            rangoMax = 10;
            break;
        case "Doble Estandar":
            rangoMin = 11;
            rangoMax = 28;
            break;
        case "Doble Superior":
            rangoMin = 29;
            rangoMax = 36;
            break;
        case "Superior Family Plan":
            rangoMin = 37;
            rangoMax = 46;
            break;
        case "Suite Doble":
            rangoMin = 47;
            rangoMax = 48;
            break;
    }

    // 4. Imprimir Cabecera
    System.out.printf("%-17s", "Habitación");
    for (Date fecha : listaFechas) {
        System.out.printf("%-17s", formatter.format(fecha));
    }
    System.out.println();

    // 5. ----- LÓGICA PRINCIPAL (MODIFICADA) -----
    // Iteramos por cada NÚMERO de habitación que queremos mostrar
    for (int numHab = rangoMin; numHab <= rangoMax; numHab++) {
        
        System.out.printf("%-17s", "Hab " + numHab);

        // Ahora, iteramos por cada FECHA (columna)
        for (Date fecha : listaFechas) {
            String estadoDelDia = "disponible"; // Estado por defecto

            // Buscamos en TODAS las líneas del CSV
            // para ver cuál aplica a esta 'fecha' y a esta 'numHab'
            for (String[] h : habitacionesCSV) {
                int csvNumHab;
                try {
                    csvNumHab = Integer.parseInt(h[0].trim());
                } catch (NumberFormatException e) {
                    continue; // Saltar línea con número de hab inválido
                }

                // Si la línea del CSV no es para la habitación actual, la ignoramos
                if (csvNumHab != numHab) {
                    continue;
                }

                // Es la habitación correcta, vemos si la fecha aplica
                String csvEstado = h[1].trim();
                String desdeStr = h[5].trim();
                String hastaStr = h[6].trim();

                Date csvDesde = null;
                Date csvHasta = null;

                if (!desdeStr.isEmpty()) csvDesde = funcionesUtiles.convertirStringADate(desdeStr);
                if (!hastaStr.isEmpty()) csvHasta = funcionesUtiles.convertirStringADate(hastaStr);

                boolean aplicaHoy = false;

                // Caso 1: Estado permanente (sin fechas, ej: "fueraDeServicio")
                if (csvDesde == null || csvHasta == null) {
                    aplicaHoy = true;
                } 
                // Caso 2: Estado con rango de fechas
                else if ((fecha.equals(csvDesde) || fecha.after(csvDesde)) &&
                         (fecha.equals(csvHasta) || fecha.before(csvHasta))) {
                    aplicaHoy = true;
                }

                // Si esta línea del CSV aplica, comparamos su prioridad
                if (aplicaHoy) {
                    if (getPrioridadEstado(csvEstado) > getPrioridadEstado(estadoDelDia)) {
                        estadoDelDia = csvEstado;
                    }
                }
            } // Fin del bucle por el CSV

            // Imprimimos el estado de mayor prioridad encontrado para esta celda
            System.out.printf("%-17s", estadoDelDia);

        } // Fin del bucle por las fechas
        System.out.println(); // Fin de la fila para esta habitación
    } // Fin del bucle por las habitaciones
}

/**
 * Define la prioridad de un estado. Un número más alto
 * tiene mayor prioridad (se mostrará sobre los otros).
 */
private int getPrioridadEstado(String estado) {
    switch (estado.toLowerCase()) {
        case "fueradeservicio": // O como lo tengas escrito
        case "fuera de servicio":
            return 4;
        case "ocupada":
            return 3;
        case "reservada":
            return 2;
        case "disponible":
            return 1;
        default:
            return 0; // Otros estados
    }
}
@Override
public void muestraEstado(Date desdeFecha, Date hastaFecha) {
    throw new UnsupportedOperationException("Unimplemented method 'muestraEstado'");
}

    /* 
    public void mostrarGrillaHabitaciones(String tipoHabitacion, Date desdeFecha, Date hastaFecha) {
        String FECHA_FORMATO = "dd/MM/yyyy";
        String NOMBRE_ARCHIVO = "infoHabitaciones.csv";
        String SEPARADOR_CSV = ",";
        SimpleDateFormat formatter = new SimpleDateFormat(FECHA_FORMATO);
        FuncionesUtiles funcionesUtiles = new FuncionesUtiles();

        // Generar lista de fechas entre desde y hasta (una por día)
        List<Date> listaFechas = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(desdeFecha);

        while (!cal.getTime().after(hastaFecha)) {
            listaFechas.add(cal.getTime());
            cal.add(Calendar.DATE, 1);
        }

        // Leer habitaciones del CSV
        List<String[]> habitaciones = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(NOMBRE_ARCHIVO))) {
            // Saltar encabezado
            br.readLine();

            String linea;
            while ((linea = br.readLine()) != null) {
                // Usamos split con -1 para conservar campos vacíos (por ejemplo: ",,")
                String[] campos = linea.split(SEPARADOR_CSV, -1);
                if (campos.length >= 7) {
                    habitaciones.add(campos);
                } else {
                    System.err.println("Línea inválida (faltan columnas): " + linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            return;
        }

        // Determinar rango de habitaciones según tipo
        int rangoMin = 0;
        int rangoMax = 0;

        switch (tipoHabitacion) {
            case "Individual Estandar":
                rangoMin = 1;
                rangoMax = 10;
                break;
            case "Doble Estandar":
                rangoMin = 11;
                rangoMax = 28;
                break;
            case "Doble Superior":
                rangoMin = 29;
                rangoMax = 36;
                break;
            case "Superior Family Plan":
                rangoMin = 37;
                rangoMax = 46;
                break;
            case "Suite Doble":
                rangoMin = 47;
                rangoMax = 48;
                break;
        }

        // Cabecera
        System.out.printf("%-17s", "Habitación");
        for (Date fecha : listaFechas) {
            System.out.printf("%-17s", formatter.format(fecha));
        }
        System.out.println();

        // Imprimir cada habitación (filtrando por rango)
        for (String[] h : habitaciones) {
            String numHabStr = h[0].trim();
            int numHab;

            try {
                numHab = Integer.parseInt(numHabStr);
            } catch (NumberFormatException e) {
                System.err.println("Número de habitación inválido: " + numHabStr);
                continue;
            }

            // Filtrar según el tipo de habitación
            if (numHab < rangoMin || numHab > rangoMax) {
                continue;
            }

            String estadoHabitacion = h[1].trim(); // reservada, disponible, fueraDeServicio, ocupada
            String desdeStr = h[5].trim();
            String hastaStr = h[6].trim();

            // Intentar convertir las fechas (si no están vacías)
            Date desdeFechaHabitacion = null;
            Date hastaFechaHabitacion = null;

            if (!desdeStr.isEmpty()) {
                desdeFechaHabitacion = funcionesUtiles.convertirStringADate(desdeStr);
            }
            if (!hastaStr.isEmpty()) {
                hastaFechaHabitacion = funcionesUtiles.convertirStringADate(hastaStr);
            }

            System.out.printf("%-17s", "Hab " + numHab);

            // Caso 1: habitación sin fechas (disponible o fuera de servicio)
            if (desdeFechaHabitacion == null || hastaFechaHabitacion == null) {
                for (int i = 0; i < listaFechas.size(); i++) {
                    System.out.printf("%-17s", estadoHabitacion);
                }
                System.out.println();
                continue;
            }

            // Caso 2: habitación con rango de fechas válido
            for (Date fecha : listaFechas) {
                String estadoDelDia;

                if ((fecha.equals(desdeFechaHabitacion) || fecha.after(desdeFechaHabitacion))
                        && (fecha.equals(hastaFechaHabitacion) || fecha.before(hastaFechaHabitacion))) {
                    estadoDelDia = estadoHabitacion; // Ej: "reservada" o "ocupada"
                } else {
                    estadoDelDia = "disponible";
                }

                System.out.printf("%-17s", estadoDelDia);
            }

            System.out.println();
        }
    }
    */
}
