package Classes.Huesped;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;
import java.text.SimpleDateFormat;

import Classes.Direccion.DireccionDTO;
import Classes.FuncionesUtiles;
import Classes.Huesped.GestorHuesped;

import static java.lang.Class.forName;

public class HuespedDAO implements HuespedDAOInterfaz {
    private static HuespedDAO instancia; // única instancia

    private HuespedDAO() { }

    /**
     *
     * @return
     */
    public static synchronized HuespedDAO getInstancia() {
        if (instancia == null) {
            instancia = new HuespedDAO();
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

    /**
     * se verifica el documento si existe
     * @param huespedDTO
     * @return
     */
    public boolean verificarDocumento(HuespedDTO huespedDTO){
    /*
     * Chequea que el Tipo y Número de Documento no existan en el archivo de registro.
     */
    String tipoDocBuscado = huespedDTO.getTipoDocumento().trim();
    String numDocBuscado = huespedDTO.getNumeroDocumento().trim();
    String RUTA_ARCHIVO = "infoBuscarHuespedes.csv";
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

    /**
     * se registra un huesped al archivo
     * @param huespedDTO
     */

    public void registrarHuesped(HuespedDTO huespedDTO){
        /*
         AGREGAR EL HUESPEDDTO QUE LLEGA A LA BD DE HUESPEDES
         */
        String RUTA_ARCHIVO="infoBuscarHuespedes.csv"; // esta en la misma carpera

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

    /**
     * se buscan los datos del huesped
     * @param nombreHuesped
     * @param apellidoHuesped
     * @param tipoDoc
     * @param numDoc
     * @return
     */

    //cumple con la consigna del tp stream/lambda
    public HuespedDTO buscarDatos(String nombreHuesped, String apellidoHuesped, String tipoDoc, String numDoc) {
        String rutaArchivo = "infoBuscarHuespedes.csv";
        List<HuespedDTO> listaHuespedes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            List<String> lineas = br.lines().skip(1).toList(); //leo todo el archivo por completo

            //Uso de Stream y lambda para leer el archivo
            listaHuespedes = lineas.stream()
                    .map(linea -> linea.split(","))
                    .filter(datos -> datos.length >= 19) //por lo menos tiene que haber 19 datos
                    .filter(datos -> {
                        String apellido = datos[0].trim();
                        String nombre = datos[1].trim();
                        String tipo = datos[2].trim();
                        String documento = datos[3].trim();

                        boolean coincideNombre = (nombreHuesped == null || nombreHuesped.isEmpty()) || nombre.equalsIgnoreCase(nombreHuesped);
                        boolean coincideApellido = (apellidoHuesped == null || apellidoHuesped.isEmpty()) || apellido.equalsIgnoreCase(apellidoHuesped);
                        boolean coincideTipo = (tipoDoc == null || tipoDoc.isEmpty()) || tipo.equalsIgnoreCase(tipoDoc);
                        boolean coincideDocumento = (numDoc == null || numDoc.isEmpty()) || documento.equalsIgnoreCase(numDoc);

                        return coincideNombre && coincideApellido && coincideTipo && coincideDocumento;
                    }) //me fijo que coincida algun dato y si no se ingreso ninguno entonces envio todos los huespedes
                    .map(datos -> {
                        HuespedDTO huespedDTO = new HuespedDTO();
                        huespedDTO.setApellido(datos[0].trim());
                        huespedDTO.setNombre(datos[1].trim());
                        huespedDTO.setTipoDocumento(datos[2].trim());
                        huespedDTO.setNumeroDocumento(datos[3].trim());
                        huespedDTO.setCuit(datos[4].trim());
                        huespedDTO.setPosicionIva(datos[5].trim());
                        try {
                            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                            huespedDTO.setFechaNacimiento(formato.parse(datos[6].trim()));
                        } catch (ParseException e) {
                            System.out.println("Error al convertir la fecha: " + datos[6]);
                        }
                        huespedDTO.setTelefono(datos[15].trim());
                        huespedDTO.setEmail(datos[16].trim());
                        huespedDTO.setDireccionHuesped(getDireccionDTO(datos));
                        huespedDTO.setOcupacion(datos[17].trim());
                        huespedDTO.setNacionalidad(datos[18].trim());
                        return huespedDTO;
                    }) //el/los huesped/es que coincide cargo todos sus datos y lo retorno
                    .toList();

            if (listaHuespedes.isEmpty()) {
                System.out.println("0 coincidencias");
                return new HuespedDTO();
            }

            // muestro todos los huespedes que hayan coincidido
            //aunque tome todos los datos solo muestro los de busqueda ya que los demas datos son para cu ModificarHUesped
            for (int i = 0; i < listaHuespedes.size(); i++) {
                HuespedDTO h = listaHuespedes.get(i);
                System.out.println((i + 1) + ": " + h.getApellido() + ", " + h.getNombre() +
                        " - " + h.getTipoDocumento() + " " + h.getNumeroDocumento());
            }

            //huesped a usar
            Scanner scanner = new Scanner(System.in);
            System.out.print("Ingrese el número del huésped que buscaba: ");
            String huespedNum = scanner.nextLine();

            while (!(FuncionesUtiles.contieneSoloNumeros(huespedNum)) ||
                    (Integer.parseInt(huespedNum) > listaHuespedes.size() || Integer.parseInt(huespedNum) <= 0)) {
                System.out.println("---- Ingrese un número válido ----");
                System.out.print("Ingrese el número del huésped que buscaba: ");
                huespedNum = scanner.nextLine();
            }

            return listaHuespedes.get(Integer.parseInt(huespedNum) - 1); //retorno todos los datos obtenidos del huesped para asi usarlo en otros cu's

        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }

        return new HuespedDTO(); //por si no hubo cincidencia
    }

    /**
     * se actualiza un huesped, de la funcion modificar huesped, dependiendo de el dni
     * @param rutaArchivo
     * @param huespedDTO
     * @param direccionDTO
     * @param dni este parametro es el dni cargado en el archivo, sino nunca lo encuentro, guardo el anterior para buscar
     */

    public boolean actualizarHuesped(Map<String, String> campos, String rutaArchivo, HuespedDTO huespedDTO, DireccionDTO direccionDTO, String tipoDoc, String dni) {
    // para formatear fecha primero
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        String fechaComoTexto = formato.format(huespedDTO.getFechaNacimiento());
        // construye la nueva línea con todos los campos
        String nuevaLinea = String.join(",",
                                    campos.values()  // toma todos los valores del Map
        );

        int indice= -1;
//leo para buscar la linea
        try {
            // se leem todas las líneas del archivo
            List<String> lineas = Files.readAllLines(Paths.get(rutaArchivo), StandardCharsets.UTF_8);

            // buscar la línea que contenga el DNI en la 3
            for (int i = 0; i < lineas.size(); i++) {
                String[] texto = lineas.get(i).split(",");


                //comparo tipo y numero de ,i huesped guardado en dto
                if (texto[2].trim().equalsIgnoreCase(tipoDoc.trim()) &&
                        texto[3].trim().equals(dni.trim())) {


                    indice = i; //agarra el indice
                    break;
                }
            }


            // Si lo encontró, reemplazar la línea y reescribir el archivo
            if (indice != -1) {
                lineas.set(indice, nuevaLinea);
                Files.write(Paths.get(rutaArchivo), lineas);
                return true;
            }
            //el huesped ya esta registrado por ende en alguna fila de el archivo esta

        } catch (IOException e) {
            System.out.println(" Error al leer o escribir el archivo:");
            e.printStackTrace();
        }


      return false;


    }


    /**
     *
     * @param datos DATOS Q LEGGAN  Y ARMO EL DTO
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

    /**
     * se elimina un huesped del archivo
     * @param huesped
     * @return
     */
    public boolean eliminarHuespued(HuespedDTO huesped) {
        String rutaArchivo = "infoBuscarHuespedes.csv";
        boolean eliminado = false;
        try  {
            // Leer todas las líneas del archivo
            List<String> lineas = Files.readAllLines(Paths.get(rutaArchivo));

            // Convertir el huesped a texto (como está guardado en el archivo)
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            String textoHuesped = huesped.getApellido() +","+ huesped.getNombre() +","+ huesped.getTipoDocumento() +","+ huesped.getNumeroDocumento() +","+ huesped.getCuit() +","+ huesped.getPosicionIva() +","+ formato.format(huesped.getFechaNacimiento() ) +","+ huesped.getDireccionHuesped().getCalle() +","+ huesped.getDireccionHuesped().getNumero() +","+ huesped.getDireccionHuesped().getDepartamento() +","+ huesped.getDireccionHuesped().getPiso() +","+ huesped.getDireccionHuesped().getCodigoPostal() +","+ huesped.getDireccionHuesped().getLocalidad() +","+ huesped.getDireccionHuesped().getProvincia() +","+ huesped.getDireccionHuesped().getPais() +","+ huesped.getTelefono() +","+ huesped.getEmail() +","+ huesped.getOcupacion() +","+ huesped.getNacionalidad();;

            // Filtrar las líneas que NO coincidan con el huesped a eliminar
            String encabezado = lineas.get(0);
            List<String> nuevasLineas = new ArrayList<>();
            nuevasLineas.add(encabezado);
            // Recorrer las líneas (empezando desde la segunda) y conservar las que NO coincidan
            for (int i = 1; i < lineas.size(); i++) {
                String linea = lineas.get(i).trim();
                if (!linea.equalsIgnoreCase(textoHuesped.trim())) {
                    nuevasLineas.add(linea);
                } else {
                    eliminado = true;
                }
            }
            // Sobrescribir el archivo con las líneas filtradas
            Files.write(Paths.get(rutaArchivo), nuevasLineas);



        } catch (IOException e) {
            e.printStackTrace();
        }
        return eliminado;
    }

    /**
     * chequea si existe huesped
     * @param huespedDTO
     * @return true o false dependiendo si existe
     */
    public boolean existeHuesped(HuespedDTO huespedDTO) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        boolean encontrado = false;
        String rutaArchivo = "infoBuscarHuespedes.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {

                String[] datos = linea.split(",");

                if (datos.length >= 20) {
                    String apellido = datos[0].trim();
                    String nombre = datos[1].trim();
                    String tipo = datos[2].trim();
                    String documento = datos[3].trim();
                    String cuit = datos[4].trim();
                    String posicionIva = datos[5].trim();
                    String FechaNacimiento = datos[6].trim();
                    String calle = datos[7].trim();
                    String numero = datos[8].trim();
                    String departamento = datos[9].trim();
                    String piso = datos[10].trim();
                    String codigoPostal = datos[11].trim();
                    String localidad = datos[12].trim();
                    String provincia = datos[13].trim();
                    String pais = datos[14].trim();
                    String telefono = datos[15].trim();
                    String email = datos[16].trim();
                    String ocupacion = datos[17].trim();
                    String nacionalidad = datos[18].trim();
                    String CheckIn = datos[19].trim();
                    String CheckOut = datos[20].trim();

                    Date fecha = formato.parse(FechaNacimiento);

                    if (apellido == huespedDTO.getApellido() && nombre == huespedDTO.getNombre() && tipo == huespedDTO.getTipoDocumento() && documento == huespedDTO.getNumeroDocumento() && cuit == huespedDTO.getCuit() && posicionIva == huespedDTO.getPosicionIva() && fecha == huespedDTO.getFechaNacimiento() && calle == huespedDTO.getDireccionHuesped().getCalle() && numero == huespedDTO.getDireccionHuesped().getNumero() && departamento == huespedDTO.getDireccionHuesped().getDepartamento() && piso == huespedDTO.getDireccionHuesped().getPiso() && codigoPostal == huespedDTO.getDireccionHuesped().getCodigoPostal() && localidad == huespedDTO.getDireccionHuesped().getLocalidad() && provincia == huespedDTO.getDireccionHuesped().getProvincia() && pais == huespedDTO.getDireccionHuesped().getPais() && telefono == huespedDTO.getTelefono() && email == huespedDTO.getEmail() && ocupacion == huespedDTO.getOcupacion() && nacionalidad == huespedDTO.getNacionalidad()) {
                        encontrado = true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("error al abrir el archivo");
            throw new RuntimeException(e);
        }
        catch (ParseException e) {
            System.out.println("error al convertir fecha");
            throw new RuntimeException(e);
        }
        return encontrado;
    }

    public boolean buscarHuespedyReemplazar(String tipodoc, String tipoDoc){
     HuespedDTO huespedDTO = new HuespedDTO();


     return true;
    }
    public boolean seAlojo(HuespedDTO huespedDTO) {
        if(huespedDTO.getEstadiaHuesped() == null){
            return false;
        }
        return true;

    }
}

