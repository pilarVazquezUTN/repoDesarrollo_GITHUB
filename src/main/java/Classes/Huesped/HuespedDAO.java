package Classes.Huesped;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Classes.Direccion.DireccionDTO;

public class HuespedDAO {
    public void delete(){
    }
    public  void create(){
    }
    public  void update(){
    }
    public  void read(){
    }

    public boolean verificarDocumento(HuespedDTO huespedDTO){
    /*
     * Chequea que el Tipo y Número de Documento no existan en el archivo de registro.
     */
    String tipoDocBuscado = huespedDTO.getTipoDocumento().trim();
    String numDocBuscado = huespedDTO.getNumeroDocumento().trim();
    String RUTA_ARCHIVO = "infoDarAltaHuespedes.txt";
    Boolean existeDoc = false;

    // Usamos try-with-resources para asegurar que el BufferedReader se cierre automáticamente
    try (BufferedReader br = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
        String linea;
        
        while ((linea = br.readLine()) != null) {
            // Dividir la línea por comas
            String[] partes = linea.split(",");
            
            // Verificamos que haya al menos 4 campos (0, 1, 2, 3)
            if (partes.length >= 4) {
                
                // 2. CORRECCIÓN DE ÍNDICES:
                // Tipo Documento es el índice 2 (tercer campo)
                String tipoDocArchivo = partes[2].trim().toUpperCase();
                // Número Documento es el índice 3 (cuarto campo)
                String numDocArchivo = partes[3].trim();

                // 3. Comparar los campos (Tipo y Número)
                if (tipoDocArchivo.equals(tipoDocBuscado) && numDocArchivo.equals(numDocBuscado)) {
                    existeDoc = true; // Coincidencia encontrada!
                    break; // Salir del bucle una vez que se encuentra la primera coincidencia
                }
            }
        }
    } catch (IOException e) {
        System.err.println("ERROR: No se pudo acceder o leer el archivo: " + RUTA_ARCHIVO);
        System.err.println("fijate que sea la rutaaa!!!");
        // Devolvemos 'false' para que el sistema asuma que no está duplicado si el archivo no es accesible.
    } 
    
    return existeDoc; 
}

    public static void registrarHuesped(HuespedDTO huespedDTO){
        /*
         AGREGAR EL HUESPEDDTO QUE LLEGA A LA BD DE HUESPEDES
         */
        String RUTA_ARCHIVO="infoDarAltaHuespedes.txt"; // esta en la misma carpera

        // 1. Crear el formateador de fechas para java.util.Date
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        
        // 2. Obtener y formatear datos (incluyendo la fecha y la dirección)
        String nombre = huespedDTO.getNombre();
        String apellido = huespedDTO.getApellido();
        String tipoDoc = huespedDTO.getTipoDocumento();
        String numDoc = huespedDTO.getNumeroDocumento();
        String cuit = huespedDTO.getCuit();
        String posIva = huespedDTO.getPosicionIva();
        String telefono = huespedDTO.getTelefono();
        String email = huespedDTO.getEmail();
        String ocupacion = huespedDTO.getOcupacion();
        String nacionalidad = huespedDTO.getNacionalidad();
        
        // Manejar la fecha de nacimiento (puede ser null si hubo error de parseo)
        Date fechaNac = huespedDTO.getFechaNacimiento();
        String fechaNacStr = (fechaNac != null) ? dateFormatter.format(fechaNac) : "NULL";

        // Obtener y formatear datos de Dirección (Asumimos que no es null después de ingresarDatos)   
        String calle = huespedDTO.getDireccionHuesped().getCalle();
        String numeroCalle = huespedDTO.getDireccionHuesped().getNumero();
        String departamento = huespedDTO.getDireccionHuesped().getDepartamento();
        String piso = huespedDTO.getDireccionHuesped().getPiso();
        String codPostal = huespedDTO.getDireccionHuesped().getCodigoPostal();
        String localidad = huespedDTO.getDireccionHuesped().getLocalidad();
        String provincia = huespedDTO.getDireccionHuesped().getProvincia();
        String pais = huespedDTO.getDireccionHuesped().getPais();

        // 3. Concatenar todos los datos en una sola línea separada por comas (CSV)
        String lineaDatos = String.join(",", 
            nombre, apellido, tipoDoc, numDoc, cuit, posIva, fechaNacStr,  
            calle, numeroCalle, departamento, piso, codPostal, localidad, provincia, pais, 
            telefono, email, ocupacion, nacionalidad
        );

        // 4. Escribir la línea en el archivo
        try (PrintWriter out = new PrintWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            
            // out.println() escribe la cadena y agrega un salto de línea al final
            out.println(lineaDatos); 
            
        } catch (IOException e) {
            System.err.println("ERROR al guardar el huésped en el archivo: " + e.getMessage());
        }
    }
    

    public static HuespedDTO buscarDatos(String nombreHuesped, String apellidoHuesped, String tipoDoc,String numDoc){
        String rutaArchivo = "infoBuscarHuespedes.txt"; // Cambia por la ruta real de tu archivo
        boolean encontrado = false;
        HuespedDTO huespedRetorno = new HuespedDTO();
        HuespedDTO huespedDTO;
        List<HuespedDTO> listaHuespedes = new ArrayList<>();


        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            int i = 0;
            while ((linea = br.readLine()) != null) {

                String[] datos = linea.split(",");

                if (datos.length >= 4) {
                    String nombre = datos[0].trim();
                    String apellido = datos[1].trim();
                    String tipo = datos[2].trim();
                    String documento = datos[3].trim();

                    // Coincide si el campo ingresado no está vacío y coincide con el del archivo DAO
                    boolean coincideNombre = nombreHuesped.isEmpty() || nombre.equalsIgnoreCase(nombreHuesped);
                    boolean coincideApellido = apellidoHuesped.isEmpty() || apellido.equalsIgnoreCase(apellidoHuesped);
                    boolean coincideTipo = tipoDoc.isEmpty() || tipo.equalsIgnoreCase(tipoDoc);
                    boolean coincideDocumento = numDoc.isEmpty() || documento.equalsIgnoreCase(numDoc);

                    // Mostrar si todos los campos ingresados coinciden
                    if (coincideNombre && coincideApellido && coincideTipo && coincideDocumento) {
                        i++;
                        huespedDTO = new HuespedDTO();
                        System.out.println((i)+":");
                        System.out.println("  Nombre: " + nombre);
                        huespedDTO.setNombre(nombre);
                        System.out.println("  Apellido: " + apellido);
                        huespedDTO.setApellido(apellido);
                        System.out.println("  Tipo documento: " + tipo);
                        huespedDTO.setTipoDocumento(tipo);
                        System.out.println("  N° documento: " + documento);
                        huespedDTO.setNumeroDocumento(documento);

                        //sigo cargando los datos del huesped
                        String fechaTexto = datos[4].trim(); // "20/05/1995"
                        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

                        try {
                            Date fNacimiento = formato.parse(fechaTexto);
                            huespedDTO.setFechaNacimiento(fNacimiento);
                        } catch (ParseException e) {
                            System.out.println("Error al convertir la fecha: " + fechaTexto);
                        }

                        huespedDTO.setTelefono(datos[5].trim());
                        huespedDTO.setEmail(datos[6].trim());

                        DireccionDTO direccionDTO = getDireccionDTO(datos);
                        huespedDTO.setDireccionHuesped(direccionDTO);
                        //aca hago el set en huesped

                        huespedDTO.setCuit(datos[15].trim());
                        huespedDTO.setPosicionIva(datos[16].trim());
                        huespedDTO.setOcupacion(datos[17].trim());
                        huespedDTO.setNacionalidad(datos[18].trim());



                        System.out.println("---------------------------");
                        encontrado = true;
                        listaHuespedes.add(huespedDTO);
                    }
                }
            }
            if (!encontrado)
            {
                System.out.println(i+" coincidencias"); 
            }else
            {
                Scanner scanner = new Scanner(System.in);
                System.out.println("ingrese el numero del huesped que buscaba: ");
                String huespedNum = scanner.nextLine();
                while(Integer.parseInt(huespedNum)>i || Integer.parseInt(huespedNum)<=0){
                    System.out.println("----ingrese un numero valido----- ");
                    System.out.println("ingrese el numero del huesped que buscaba: ");
                    huespedNum = scanner.nextLine();
                }
                return listaHuespedes.get(Integer.parseInt(huespedNum)-1);
            }
            //4.A.1. El sistema pasa a ejecutar el CU11 “Dar alta de Huésped” 4.A.2 El CU termina. 
        }
        catch (IOException e) {
        System.out.println("Error al leer el archivo: " + e.getMessage());
    }
        return huespedRetorno;
    }

    /**
     *
     * @param rutaArchivo
     * @param huespedDTO
     * @param direccionDTO
     * @param dni este parametro es el dni cargado en el archivo, sino nunca lo encuentro, guardo el anterior para buscar
     */
    public void actualizarHuesped(String rutaArchivo, HuespedDTO huespedDTO, DireccionDTO direccionDTO, String dni) {
    // para formatear fecha primero
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        String fechaComoTexto = formato.format(huespedDTO.getFechaNacimiento());


            // construye la nueva línea con todos los campos
                    String nuevaLinea = String.join(",",
                            huespedDTO.getNombre(),
                            huespedDTO.getApellido(),
                            huespedDTO.getTipoDocumento(),
                            huespedDTO.getNumeroDocumento(),
                            fechaComoTexto,
                            huespedDTO.getTelefono(),
                            huespedDTO.getEmail(),
                            direccionDTO.getCalle(),
                            direccionDTO.getNumero(),
                            direccionDTO.getPiso(),
                            direccionDTO.getDepartamento(),
                            direccionDTO.getCodigoPostal(),
                            direccionDTO.getLocalidad(),
                            direccionDTO.getProvincia(),
                            direccionDTO.getPais(),
                            huespedDTO.getCuit(),
                            huespedDTO.getPosicionIva(),
                            huespedDTO.getOcupacion(),
                            huespedDTO.getNacionalidad()
                    );
int indice= -1;
//leo para buscar la linea
        try {
            // se leem todas las líneas del archivo
            List<String> lineas = Files.readAllLines(Paths.get(rutaArchivo));

            // buscar la línea que contenga el DNI en la 3
            for (int i = 0; i < lineas.size(); i++) {
                String[] campos = lineas.get(i).split(",");
                if (campos.length > 3 && campos[3].equals(dni)) {
                    indice = i; //agarra el indice
                    break;
                }
            }

            // Si lo encontró, reemplazar la línea y reescribir el archivo
            if (indice != -1) {
                lineas.set(indice, nuevaLinea);
                Files.write(Paths.get(rutaArchivo), lineas);
                System.out.println(" Cambios realizados correctamente " );
            }
            //el huesped ya esta registrado por ende en alguna fila de el archivo esta

        } catch (IOException e) {
            System.out.println(" Error al leer o escribir el archivo:");
            e.printStackTrace();
        }





    }


    /**
     *
     * @param datos datos q le llegan y hace el set
     * @return
     */



    private static DireccionDTO getDireccionDTO(String[] datos) {
        DireccionDTO direccionDTO = new DireccionDTO();
        direccionDTO.setCalle(datos[7].trim());
        direccionDTO.setNumero(datos[8].trim());
        direccionDTO.setDepartamento(datos[9].trim());
        direccionDTO.setPiso(datos[10].trim());
        direccionDTO.setCodigoPostal(datos[11].trim());
        direccionDTO.setLocalidad(datos[12].trim());
        direccionDTO.setProvincia(datos[13].trim());
        direccionDTO.setPais(datos[14].trim());
        return direccionDTO;
    }



}

