package Main;
import java.io.IOException;
import java.text.ParseException; 
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Predicate;

import Classes.Huesped.HuespedDAO;
import Classes.Huesped.HuespedDTO;
import Classes.Usuario.GestorUsuario;
import Classes.Usuario.UsuarioDAO;
import Classes.Usuario.UsuarioDTO;
import Classes.Habitacion.GestorHabitacion;
import Classes.Huesped.GestorHuesped;
import Classes.Direccion.DireccionDTO;
import Classes.Validador;






public class App {
    static GestorHuesped gestorHuesped=new GestorHuesped();
    static GestorUsuario gestorUsuario= new GestorUsuario();
    static GestorHabitacion gestorHabitacion= new GestorHabitacion();
    
    public static void main(String[] args) {
        Bienvenida();
    }

    public static void Bienvenida(){
        Scanner scanner = new Scanner(System.in);
        Integer opcion;

        clearConsola();
        do{
            System.out.println("BIENVENIDO A LA GESTION DEL HOTEL. \n Presione 1 para Autenticar su Usuario. ");
            opcion=scanner.nextInt();
        } while (opcion!=1);
        System.out.println("\033[H\033[2J"); //cambio erne no anda igual
        System.out.flush();//cambio erne
        autenticarHuesped();

    }

    public static void Menu(){
        System.out.println("--------------- MENU ----------------");
        System.out.println("\n Opciones: ");
        System.out.println("1. Buscar Huesped"); 
        System.out.println("2. Dar De Alta Huesped");
        //System.out.println("3. Dar de Baja Huesped");
        System.out.println("3. Reservar Habitacion");
        System.out.print("--- Ingrese una opción: "); 

        ingresaOpcion();

    }

    public static void ingresaOpcion(){
        Scanner scanner = new Scanner(System.in);
        boolean entradaValida=false;
        int opcion= scanner.nextInt();
        clearConsola();
        do{
            if(opcion<1 || opcion>4){
                clearConsola();
                System.out.println("--------------- Ingrese una opcion correcta!");
                Menu();
            } else{
                entradaValida=true;
            }
        } while(!entradaValida);

        clearConsola();
        switch (opcion) {
            case 1://buscar huesped
                System.out.println("BUSCAR HUESPED \n");
                buscarHuesped();
                break;
            case 2://dar de alta huesped
                System.out.println("DAR DE ALTA HUESPED \n");
                darAltaHuesped();                
                break;
            //case 3://dar de baja huesped. POR LAS DUDAS LO DEJOR PERO ME PARECE QUE HAY QUE SACARLO
               // System.out.println("DAR DE BAJA HUESPED \n");
                // este no iria porque dar de baja al huesped se ejecuta despues del cu10 modificarhuesped
                //break;
            case 3: //reservar habitacion
                reservarHabitacion();
                break;
        }
    }

    public static void autenticarHuesped(){
        Scanner scanner = new Scanner(System.in);
        String nombreUsuario;
        String contrasenaUsuario;

        System.out.print("Ingrese su nombre: ");
        nombreUsuario = scanner.next();

        // Validación de contraseña con ocultación (*)
        boolean contrasenaValida = false;
        do {
            System.out.print("Ingrese su contraseña: ");
            contrasenaUsuario = leerContrasenaOculta();

            if (!esContrasenaValida(contrasenaUsuario)) {
                System.out.println("Contraseña inválida. \n"); //Debe tener al menos 5 letras y 3 números no iguales ni consecutivos.
            } else {
                contrasenaValida = true;
            }
        } while (!contrasenaValida);

        // Creamos el UsuarioDTO para pasarlo al DAO
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre(nombreUsuario);
        usuarioDTO.setContrasena(contrasenaUsuario);

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        clearConsola();

        // Llamamos al Gestor
        if (gestorUsuario.autenticarUsuario(usuarioDAO, usuarioDTO)) {
            System.out.println("Usuario Encontrado. Acceso concedido.\n");
            Menu();
        } else {
            System.out.println("Usuario no encontrado. Se vuelve a Autenticación de Usuario.\n");
            autenticarHuesped();
        }
    }

    public static boolean esContrasenaValida(String contrasena) {
        if (contrasena == null) return false;

        int letras = 0;
        List<Integer> numeros = new ArrayList<>();

        for (char c : contrasena.toCharArray()) {
            if (Character.isLetter(c)) letras++;
            else if (Character.isDigit(c)) numeros.add(Character.getNumericValue(c));
        }

        if (letras < 5 || numeros.size() < 3) return false; // mínimo 5 letras y 3 números

        // Revisar que no haya números consecutivos crecientes o decrecientes
        for (int i = 0; i < numeros.size() - 2; i++) {
            int a = numeros.get(i);
            int b = numeros.get(i + 1);
            int c = numeros.get(i + 2);

            // Consecutivos crecientes
            if (b == a + 1 && c == b + 1) return false;
            // Consecutivos decrecientes
            if (b == a - 1 && c == b - 1) return false;
        }

        // Revisar que no haya números repetidos
        Set<Integer> setNumeros = new HashSet<>(numeros);
        if (setNumeros.size() != numeros.size()) return false;

        return true;
    }



    public static String leerContrasenaOculta() {
        StringBuilder contrasena = new StringBuilder();
        try {
            // Leer carácter por carácter
            int caracter;
            while ((caracter = System.in.read()) != '\n' && caracter != '\r') {  // Hasta Enter
                if (caracter == '\b') {  // Backspace: borrar último "*"
                    if (contrasena.length() > 0) {
                        contrasena.deleteCharAt(contrasena.length() - 1);
                        System.out.print("\b \b");  // Borra el "*" en pantalla
                    }
                } else {
                    contrasena.append((char) caracter);
                    System.out.print("*");  // Imprime "*" en lugar del carácter real
                }
            }
            System.out.println();  // Nueva línea después de Enter
        } catch (IOException e) {
            System.err.println("Error al leer la contraseña: " + e.getMessage());
        }
        return contrasena.toString();
    }

    public static void darAltaHuesped(){
        Scanner scanner = new Scanner(System.in);
        HuespedDTO huespedDTO = new HuespedDTO();
        Boolean camposVacios=false;//para controlar cuando ingresa mal los campos luego del 1er ingreso
        DireccionDTO direccionDTO=new DireccionDTO();
        
        ingresarDatos(camposVacios,huespedDTO,direccionDTO);
        gestorHuesped.registrarHuesped(huespedDTO);
        
        Integer opcion;
        do{
            System.out.println("El huésped " + huespedDTO.getNombre() +","+huespedDTO.getApellido() + 
            " ha sido satisfactoriamente cargado al sistema. \n ¿Desea cargar otro? \n" +
            "1. si \n 2. no");
        
            opcion= scanner.nextInt();
        } while (opcion!=1 && opcion!=2);
        if(opcion==1){
            //vuelve a que cargue mas huespedes
            darAltaHuesped();
        } else{
            clearConsola();
            Menu();
        }
    }
    public static HuespedDTO ingresarDatos(Boolean camposVacios, HuespedDTO huespedDTO, DireccionDTO direccionDTO){
        //HuespedDTO huespedDTO = new HuespedDTO();
        Scanner scanner = new Scanner(System.in);
        Date fechaNacDTO;

        if(camposVacios){
            if(huespedDTO.getApellido().isEmpty()){
                System.out.print("Error en campo. Ingrese CORRECTAMENTE su apellido: ");
                huespedDTO.setApellido(scanner.nextLine().toUpperCase());
            } else{
                System.out.println("Su Apellido "+huespedDTO.getApellido());
            }
        } else{
            System.out.print("Ingrese su apellido: ");
            huespedDTO.setApellido(scanner.nextLine().toUpperCase());
        }


        if(camposVacios){
            if(huespedDTO.getNombre().isEmpty()){
                System.out.print("Error en campo. Ingrese CORRECTAMENTE su nombre: ");
                huespedDTO.setNombre(scanner.nextLine().toUpperCase());
            } else{
                System.out.println("Su nombre: "+huespedDTO.getNombre());    
            }
        } else{
            System.out.print("Ingrese su nombre: ");
            huespedDTO.setNombre(scanner.nextLine().toUpperCase());
        } 

        
        if(camposVacios){
            if(huespedDTO.getTipoDocumento().isEmpty()){
                System.out.print("Error en campo. Ingrese CORRECTAMENTE su Tipo de Documento: ");
                huespedDTO.setTipoDocumento(scanner.nextLine().toUpperCase());
            } else{
                System.out.println("Su Tipo de Documento: "+huespedDTO.getTipoDocumento());    
            }
        } else{
            System.out.print("Ingrese Tipo de Documento: ");
            huespedDTO.setTipoDocumento(scanner.nextLine().toUpperCase());
        } 
        
        if(camposVacios){
            if(huespedDTO.getNumeroDocumento().isEmpty()){
                System.out.print("Error en campo. Ingrese CORRECTAMENTE su Numero de Documento: ");
                huespedDTO.setNumeroDocumento(scanner.nextLine().toUpperCase());
            } else{
                System.out.println("Su Numero de Documento: "+huespedDTO.getNumeroDocumento());    
            }
        } else{
            System.out.print("Ingrese su numero de documento: ");
            huespedDTO.setNumeroDocumento(scanner.nextLine());
        } 
        
        if(huespedDTO.getCuit()=="Responsable Inscripto" && huespedDTO.getCuit().isEmpty()){
            System.out.println("Error en campo. Ingrese un CUIT obligatoraimente: ");
        } else {
            System.out.print("Ingrese su CUIT: ");
        } huespedDTO.setCuit(scanner.nextLine());
        
        String posIva="CONSUMIDOR FINAL";
        if(camposVacios){
            if(huespedDTO.getPosicionIva().isEmpty()){
                System.out.print("Error en campo. Ingrese CORRECTAMENTE su Posicion Frente al IVA: ");
                posIva=scanner.nextLine().toUpperCase();
            } else{
                System.out.println("Su Posicion Frente al IVA: "+huespedDTO.getPosicionIva());    
            }
        } else{
            System.out.print("Ingrese su Posicion Frente al IVA: ");
            posIva=scanner.nextLine().toUpperCase();
        } 
        if(posIva.isEmpty()){
            posIva="CONSUMIDOR FINAL";
        }
        huespedDTO.setPosicionIva(posIva);
        
        if(camposVacios){
            if(huespedDTO.getFechaNacimiento()==null){
                System.out.print("Error en campo. Ingrese CORRECTAMENTE su Fecha de Nacimiento (dd/MM/yyyy): ");
                //huespedDTO.set(scanner.nextLine().toUpperCase());
                String fechaNacStr = scanner.nextLine();
                // Definir el formato
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                formatter.setLenient(false); // Validar la fecha estrictamente
                try {
                    // Intentamos la conversión UNA SOLA VEZ
                    fechaNacDTO = formatter.parse(fechaNacStr);
                    huespedDTO.setFechaNacimiento(fechaNacDTO);
                    //System.out.println("Fecha ingresada y guardada.");
                } catch (ParseException e) {
                    // Si la conversión falla, informamos y la fechaNacDTO queda como null (o el valor que tenía antes)
                    //System.err.println("ERROR: El formato de fecha ingresado es INCORRECTO.");
                    huespedDTO.setFechaNacimiento(null); // Opcional: Asegurar que el DTO tenga null
                }
            } else{
                    System.out.println("Su Fecha de Nacimiento: "+huespedDTO.getFechaNacimiento());    
                }
        } else{
            System.out.print("Ingrese su Fecha de Nacimiento (dd/MM/yyyy): ");
            String fechaNacStr = scanner.nextLine();
            // Definir el formato
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            formatter.setLenient(false); // Validar la fecha estrictamente
            try {
                // Intentamos la conversión UNA SOLA VEZ
                fechaNacDTO = formatter.parse(fechaNacStr);
                huespedDTO.setFechaNacimiento(fechaNacDTO);
                //System.out.println("Fecha ingresada y guardada.");
            } catch (ParseException e) {
                // Si la conversión falla, informamos y la fechaNacDTO queda como null (o el valor que tenía antes)
                //System.err.println("ERROR: El formato de fecha ingresado es INCORRECTO. La fecha no fue guardada.");
                huespedDTO.setFechaNacimiento(null); // Opcional: Asegurar que el DTO tenga null
            }
        } 
        
        
        
        if(camposVacios){
            if(direccionDTO.getCalle().isEmpty()){
                System.out.print("Error en campo. Ingrese CORRECTAMENTE su Calle: ");
                direccionDTO.setCalle(scanner.nextLine().toUpperCase());
            } else{
                System.out.println("Su Calle: "+ direccionDTO.getCalle());    
            }
        } else{
            System.out.print("Ingrese su Calle: ");
            direccionDTO.setCalle(scanner.nextLine().toUpperCase());
            //System.out.println(direccionDTO.getCalle());
        } 
        
        if(camposVacios){
            if(direccionDTO.getNumero().isEmpty()){
                System.out.print("Error en campo. Ingrese CORRECTAMENTE su Número de Calle: ");
                direccionDTO.setNumero(scanner.nextLine());
            } else{
                System.out.println("Su Numero de Calle: "+direccionDTO.getNumero());    
            }
        } else{
            System.out.print("Ingrese su Número de Calle: ");
            direccionDTO.setNumero(scanner.nextLine());
        }
        
        if(camposVacios){
            if(direccionDTO.getDepartamento().isEmpty()){
                System.out.print("Error en campo. Ingrese CORRECTAMENTE su Departamento: ");
                direccionDTO.setDepartamento(scanner.nextLine().toUpperCase());
            } else{
                System.out.println("Su Departamento: "+direccionDTO.getDepartamento());    
            }
        } else{
            System.out.print("Ingrese su departamento: ");
            direccionDTO.setDepartamento(scanner.nextLine().toUpperCase());
        } 
        
        if(camposVacios){
            if(direccionDTO.getPiso().isEmpty()){
                System.out.print("Error en campo. Ingrese CORRECTAMENTE su Piso: ");
                direccionDTO.setPiso(scanner.nextLine()); 
            } else{
                System.out.println("Su Piso: "+direccionDTO.getPiso());    
            }
        } else{
            System.out.print("Ingrese su piso: ");
            direccionDTO.setPiso(scanner.nextLine()); 
        } 
               
        if(camposVacios){
            if(direccionDTO.getCodigoPostal().isEmpty()){
                System.out.print("Error en campo. Ingrese CORRECTAMENTE su Codigo Postal: ");
                direccionDTO.setCodigoPostal(scanner.nextLine());
            } else{
                System.out.println("Su Codigo Postal: "+direccionDTO.getCodigoPostal());    
            }
        } else{
            System.out.print("Ingrese su Código Postal: ");
            direccionDTO.setCodigoPostal(scanner.nextLine());
        } 
        
        if(camposVacios){
            if(direccionDTO.getLocalidad().isEmpty()){
                System.out.print("Error en campo. Ingrese CORRECTAMENTE su Localidad: ");
                direccionDTO.setLocalidad(scanner.nextLine().toUpperCase());
            } else{
                System.out.println("Su Localidad: "+direccionDTO.getLocalidad());    
            }
        } else{
            System.out.print("Ingrese su Localidad: ");
            direccionDTO.setLocalidad(scanner.nextLine().toUpperCase());
        } 
        
        if(camposVacios){
            if(direccionDTO.getProvincia().isEmpty()){
                System.out.print("Error en campo. Ingrese CORRECTAMENTE su Provincia: ");
                direccionDTO.setProvincia(scanner.nextLine().toUpperCase()); 
            } else{
                System.out.println("Su Provincia: "+direccionDTO.getProvincia());    
            }
        } else{
            System.out.print("Ingrese su provincia: ");
            direccionDTO.setProvincia(scanner.nextLine().toUpperCase()); 
        } 
        
        if(camposVacios){
            if(direccionDTO.getPais().isEmpty()){
                System.out.print("Error en campo. Ingrese CORRECTAMENTE su Pais: ");
                direccionDTO.setPais(scanner.nextLine().toUpperCase());
            } else{
                System.out.println("Su Pais: "+direccionDTO.getPais());    
            }
        } else{
            System.out.print("Ingrese su Pais: ");
            direccionDTO.setPais(scanner.nextLine().toUpperCase());
        } 

        huespedDTO.setDireccionHuesped(direccionDTO);
        
        if(camposVacios){
            if(huespedDTO.getTelefono().isEmpty()){
                System.out.print("Error en campo. Ingrese CORRECTAMENTE su Telefono: ");
                huespedDTO.setTelefono(scanner.nextLine());
            } else{
                System.out.println("Su Telefono: "+huespedDTO.getTelefono());    
            }
        } else{
            System.out.print("Ingrese su Telefono: ");
            huespedDTO.setTelefono(scanner.nextLine());
        } 
        
        System.out.print("Ingrese su email: ");
        huespedDTO.setEmail(scanner.nextLine().toUpperCase());
        
        if(camposVacios){
            if(huespedDTO.getOcupacion().isEmpty()){
                System.out.print("Error en campo. Ingrese CORRECTAMENTE su Ocupacion: ");
                huespedDTO.setOcupacion(scanner.nextLine().toUpperCase());
            } else{
                System.out.println("Su Ocupacion: "+huespedDTO.getOcupacion());    
            }
        } else{
            System.out.print("Ingrese su Ocupación: ");
            huespedDTO.setOcupacion(scanner.nextLine().toUpperCase());
        } 
        
        if(camposVacios){
            if(huespedDTO.getNacionalidad().isEmpty()){
                System.out.print("Error en campo. Ingrese CORRECTAMENTE su Nacionalidad: ");
                huespedDTO.setNacionalidad(scanner.nextLine().toUpperCase());
            } else{
                System.out.println("Su Nacionalidad: "+huespedDTO.getNacionalidad());    
            }
        } else{
            System.out.print("Ingrese su Nacionalidad: ");
            huespedDTO.setNacionalidad(scanner.nextLine().toUpperCase());
        } 
                
        System.out.println("1. Siguiente");     
        System.out.println("2. Cancelar");

        Integer opcion =scanner.nextInt();

        if(opcion==1){
           //eligio Siguiente
            controlarCampos(huespedDTO,direccionDTO); 
        } else{
            //elige Cancelar
            do{
                System.out.println("¿Desea cancelar el alta del huésped? \n 1. si. Salir \n 2. no. Volver a cargar todos los datos");
                opcion=scanner.nextInt();
            } while (opcion!=1 && opcion!=2);
            if(opcion==1){
                //sistema vuelve al paso 6: Menu();
                clearConsola();
                Menu();
            } else{
                //sistema vuelve al paso1: muestra todos los datos nuevamente
                huespedDTO=null;
                clearConsola();
                darAltaHuesped();
            }
        }

        return huespedDTO; //FALTA VER QUE PASA SI PRESIONA CANCELAR   
    }
    public static void controlarCampos(HuespedDTO huespedDTO, DireccionDTO direccionDTO){
        //veo si algun campo obligatorio esta sin completar
        Scanner scanner = new Scanner(System.in);
        clearConsola();

        if(huespedDTO.getNombre().isEmpty() || huespedDTO.getApellido().isEmpty() || huespedDTO.getTipoDocumento().isEmpty() || 
        huespedDTO.getNumeroDocumento().isEmpty() || (huespedDTO.getDireccionHuesped()).getCalle().isEmpty() || 
        (huespedDTO.getDireccionHuesped()).getNumero().isEmpty() || (huespedDTO.getDireccionHuesped()).getDepartamento().isEmpty() || 
        (huespedDTO.getDireccionHuesped()).getPiso().isEmpty() || (huespedDTO.getDireccionHuesped()).getCodigoPostal().isEmpty() || 
        (huespedDTO.getDireccionHuesped()).getLocalidad().isEmpty() || (huespedDTO.getDireccionHuesped()).getProvincia().isEmpty() ||
        (huespedDTO.getDireccionHuesped()).getPais().isEmpty() || huespedDTO.getTelefono().isEmpty() || 
        huespedDTO.getOcupacion().isEmpty() || huespedDTO.getNacionalidad().isEmpty() || huespedDTO.getFechaNacimiento()==null){

                //llamo a que se ingresen los datos pero le digo que se ingresaron mal los campos
                ingresarDatos(true,huespedDTO,direccionDTO);
                if(verificarDocumento(huespedDTO)){
                    //se encontro un huesped existente con ese DOCUMENTO
                    System.out.println("“¡CUIDADO! El tipo y número de documento ya existen en el sistema");
                    Integer opc;
                    do {
                        System.out.println("1. ACEPTAR IGUALMENTE");
                        System.out.println("2. CORREGIR tipo de documento");
                        opc= scanner.nextInt();
                    } while (opc!=1 || opc!=2);

                    if(opc==2){
                        /*
                         se vuelve al ingresarDatos(); con foco en el campo tipo de documento
                         */
                        huespedDTO.setTipoDocumento("");
                        ingresarDatos(true, huespedDTO, direccionDTO);
                    } else {//pone ACEPTAR IGUALMENTE
                        registrarHuesped(huespedDTO, direccionDTO);
                    }
                }
            } else{
                if(verificarDocumento(huespedDTO)){
                    //se encontro un huesped existente con ese DOCUMENTO
                    System.out.println("“¡CUIDADO! El tipo y número de documento ya existen en el sistema");
                    Integer opc;
                    do {
                        System.out.println("1. ACEPTAR IGUALMENTE");
                        System.out.println("2. CORREGIR");
                        opc= scanner.nextInt();
                    } while (opc!=1 && opc!=2);

                    if(opc==2){
                        /*
                         se vuelve al ingresarDatos(); con foco en el campo tipo de documento
                         */
                        huespedDTO.setTipoDocumento("");
                        ingresarDatos(true, huespedDTO, direccionDTO);
                    } else {//pone ACEPTAR IGUALMENTE
                        registrarHuesped(huespedDTO, direccionDTO);
                    }
                } else{
                    registrarHuesped(huespedDTO,direccionDTO);
                }
                
            } 
    }
    public static boolean verificarDocumento(HuespedDTO huespedDTO){
        return gestorHuesped.verificarDocumento(huespedDTO);
    }


    public static void registrarHuesped(HuespedDTO huespedDTO, DireccionDTO direccionDTO){
        huespedDTO.setNombre(huespedDTO.getNombre());
        huespedDTO.setApellido(huespedDTO.getApellido());
        huespedDTO.setTipoDocumento(huespedDTO.getTipoDocumento());
        huespedDTO.setNumeroDocumento(huespedDTO.getNumeroDocumento());
        huespedDTO.setCuit(huespedDTO.getCuit());
        huespedDTO.setPosicionIva(huespedDTO.getPosicionIva());
        huespedDTO.setFechaNacimiento(huespedDTO.getFechaNacimiento());
        direccionDTO.setCalle((huespedDTO.getDireccionHuesped()).getCalle());
        direccionDTO.setNumero((huespedDTO.getDireccionHuesped()).getNumero());
        direccionDTO.setDepartamento((huespedDTO.getDireccionHuesped()).getDepartamento());
        direccionDTO.setPiso((huespedDTO.getDireccionHuesped()).getPiso());
        direccionDTO.setCodigoPostal((huespedDTO.getDireccionHuesped()).getCodigoPostal());
        direccionDTO.setLocalidad((huespedDTO.getDireccionHuesped()).getLocalidad());
        direccionDTO.setProvincia((huespedDTO.getDireccionHuesped()).getProvincia());
        direccionDTO.setPais((huespedDTO.getDireccionHuesped()).getPais());
        huespedDTO.setDireccionHuesped(direccionDTO);
        huespedDTO.setTelefono(huespedDTO.getTelefono());
        huespedDTO.setEmail(huespedDTO.getEmail());
        huespedDTO.setOcupacion(huespedDTO.getOcupacion());
        huespedDTO.setNacionalidad(huespedDTO.getNacionalidad());
    }

    public static void clearConsola(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    public static void buscarHuesped(){
        /*se presenta pantalla para buscar huesped 
         ingresa nombre, apellido, tipo doc, num doc
         mostrar error de algun dato invalido
         sistema blanquea campos cuando esten OK
         se vuelve al Menu
        */
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese su apellido: ");
        String apellidoHuesped = scanner.nextLine();
        System.out.print("Ingrese su nombre: ");
        String nombreHuesped = scanner.nextLine();
        System.out.print("Ingrese Tipo de documento: ");
        String tipoDoc = scanner.nextLine();
        System.out.print("Ingrese su documento: ");
        String numDoc = scanner.nextLine();
        HuespedDAO huespedDAO= new HuespedDAO();
        HuespedDTO huespedDTO = new HuespedDTO();
        huespedDTO=gestorHuesped.buscarDatos(nombreHuesped,apellidoHuesped,tipoDoc,numDoc);
        clearConsola();
        System.out.println("Huesped seleccionado: ");
        System.out.println("  Nombre: " + huespedDTO.getNombre());  
        System.out.println("  Apellido: " + huespedDTO.getApellido());
        System.out.println("  Tipo documento: " + huespedDTO.getTipoDocumento());
        System.out.println("  N° documento: " + huespedDTO.getNumeroDocumento());
        if(huespedDTO.getApellido()!=null){
            System.out.println("DESEA MODIFICAR EL HUESPED? - presione si o no ");
            if ( scanner.nextLine().equals("si")){
                modificarHuesped(huespedDTO,gestorHuesped); //aca llamo a modificar huesped con Huesped DTO Y HuespedDto debe tener todos los campos
            }
        }

        //nose si deberia llamar al gestor o a la clase
        //System.out.println("se elimino: " + gestorHuesped.eliminarHuesped(huespedDTO)); es para probar a ver si elimina pero necesito el CU 12 PARA
    }

    /**
     *
     * @param huespedDTO
     * @param gestorHuesped
     * yo aca agarro mis daots del huespedDTO, los tengo q mostrar por pantalla dsp mando al gestor para q llame al dao
     * FALTA VALIDAR LOS DATOS DE CADA CAMPO QUE MODIFICA, (hice de campos string) SI PRESIONA BORRAR ---- DEBE VOLVER A PEDIR Q INGRESE PORQ ES OBLIGATORIO
     */




    public static void modificarHuesped(HuespedDTO huespedDTO, GestorHuesped gestorHuesped) {
        String dniNOMod = huespedDTO.getNumeroDocumento(); //son los no modficados asi dsp busco en q parte de el archivo esta
        Scanner sc = new Scanner(System.in);
        String tipoNomod=huespedDTO.getTipoDocumento();

        Map<String, String> campos = new LinkedHashMap<>();
        Map<String, Predicate<String>> validadores = new HashMap<>();
        Set<String> noObligatorios = Set.of("CUIT", "email");
        gestorHuesped.modificarHuespedGestor(huespedDTO, huespedDTO.getDireccionHuesped(), "infoBuscarHuespedes.txt", campos, validadores);

        for (String campo : campos.keySet()) {  // for primeoro para apellido, nombre , asi , cada campo en el keyset
            boolean ingresado = false;
            while (!ingresado) { //aca el while para verificar cada campo y q vuelva a ingresar valor

                System.out.println("Valor de " + campo + ": " + campos.get(campo));
                System.out.print("Ingrese nuevo valor (Enter = mantener, b = borrar): ");
                String input = sc.nextLine().trim();

                if (input.isEmpty()) {
                    // Enter se mantiene valor
                    ingresado = true;

                } else if (input.equalsIgnoreCase("b")) {
                    // Quiere borrar - si es obligatorio no puede, si no esta en NO OBLIGATORIOS ES PORQ ES OBLIGATORIO
                    if (!noObligatorios.contains(campo)) {
                        System.out.println("Este campo es obligatorio y no puede quedar vacío.");
                    } else {
                        campos.put(campo, ""); // borrar valor
                        ingresado = true;
                    }
                } else {
                    // Nuevo valor ingresado
                    campos.put(campo, input);
                    ingresado = true;
                }
            }
        }

        // valida campos, pide incorrectos
        System.out.println("PARA ACEPTAR PRESIONE 1: ");
        System.out.println("PARA CANCELAR PRESIONE 2: ");
        System.out.println("PARA BORRAR PRESIONE 3: ");
        int opcion = sc.nextInt();

        if ( opcion == 1) {

        System.out.println("CAMPOS INCORRECTOS VUELVA A INGRESAR \n");
        boolean todosValidos;
        do {
            todosValidos = true;

            for (Map.Entry<String, String> entry : campos.entrySet()) {
                String campo = entry.getKey();
                String valor = entry.getValue();
                Predicate<String> validador = validadores.get(campo);



                // Si es obligatorio y vacío .inválido

                if ((!noObligatorios.contains(campo) && valor.isBlank()) || !validador.test(valor)) {

                    System.out.println("\n Valor inválido para '" + campo + "': " + valor);

                    boolean ingresado = false;

                    while (!ingresado) {
                        System.out.print("ingrese " + campo + " nuevamente (B para borrar): ");
                        String input = sc.nextLine().trim();

                        if (input.isEmpty() && noObligatorios.contains(campo)) {
                            // Enter en opcional, se puede  mantener vacío
                            ingresado = true;

                        } else if (input.equalsIgnoreCase("b")) {
                            if (!noObligatorios.contains(campo)) {
                                System.out.println("Este campo es obligatorio y no puede quedar vacío.");
                            } else {
                                campos.put(campo, "");
                                ingresado = true;
                            }
                        } else {
                            campos.put(campo, input);
                            ingresado = true;
                        }
                    }
                    todosValidos = false;
                }
            }
        } while (!todosValidos);
         //FUNCION GUILLE CHEQUEAR SI ESTA EL TIPO Y NUMERO EN EL SISTEMA EL QUE INGRESONUEVOOO
        // si hay exite el num de doc ese voy al huesped ese y lo modifico todo menos e ni
        int a=1;
       // if (huespedDao.existeHuesped()){
            System.out.println("¡CUIDADO NUMERO DOCUMENTO YA EXISTE EN EL SISTEMA");
            System.out.println("1. Aceptar igualmente");
            System.out.println("2. Corregir");
            int entrada=sc.nextInt();

            do {
                System.out.println("Entrada inválida. Debe ingresar 1 o 2.");

                System.out.println("1. Aceptar igualmente");
                System.out.println("2. Corregir");

                entrada = Integer.parseInt(sc.nextLine().trim());

            } while((entrada != 1 && (entrada != 2)));

           if ( entrada != 1){
               //pide datos y se vuelven a ingresar
                //itera y modifica el tipo y numero doc

                System.out.println("corregir datos tipo y numero de documento");
                //tenog q validar d enuevo
                System.out.println("Ingrese el tipo");
                String tipoDoc = sc.nextLine().trim();

                System.out.println("Ingrese el dni");
                String valorDoc= sc.nextLine().trim();

                if (Validador.esNumeroValido.test(valorDoc) && (Validador.esStringValido.test(tipoDoc))) {
                   //si valida guarda los calores en el map
                    campos.put("tipoDocumento", valorDoc);
                    campos.put("numeroDocumento", valorDoc);
                }
                else{
                    //opta por aceptar igualmente
                    System.out.println("la operacion a culminado con exito ");

                    //ahora llamo a geestor para q llame al dao y guarde en el archivo, le paso el dni ue esta en el archivo para poder buscar
                    gestorHuesped.modificarDatosHuespedArchivo(huespedDTO, "infoBuscarHuespedes.txt", huespedDTO.getDireccionHuesped(), tipoNomod, dniNOMod );
                }

                }

        }
        //cancelar
        else if (opcion ==2){
            System.out.println("¿Desea cancelar la modificacion del huesped?");
            System.out.println("Indique - si o no ");
            String opcion2 = sc.nextLine().trim();

            if (opcion2.equalsIgnoreCase("si")){ //muestra todo lo seleccionado
                System.out.println("Se cancela la modificacin del huesped");

            }

        }

        else if (opcion == 3){
            darBajaHuesped(huespedDTO);
        }

        System.out.println("\n Todos los campos válidos:"); //key - value
        campos.forEach((k, v) -> System.out.println(k + ": " + v));

        System.out.println("la operacion a culminado con exito ");

        //ahora llamo a geestor para q llame al dao y guarde en el archivo, le paso el dni ue esta en el archivo para poder buscar
        gestorHuesped.modificarDatosHuespedArchivo(huespedDTO, "infoBuscarHuespedes.txt",huespedDTO.getDireccionHuesped(), tipoNomod, dniNOMod );

        //huespedDAO.actualizarHuesped(rutaArchivo, huespedDTO, huespedDTO.getDireccionHuesped(), tipoDoc, dninoMod);



    }


    public static void reservarHabitacion(){
        System.out.println("RESERVAR HABITACION");
        Date desdeFecha=null, hastaFecha=null;

        Scanner scanner = new Scanner(System.in);
        String fechaIngresada;
        SimpleDateFormat formatter= new SimpleDateFormat("dd/MM/yyyy");
        formatter.setLenient(false);

         if (desdeFecha == null) {
            while (true) {
                System.out.print("Ingrese Desde Fecha (dd/MM/aaaa): ");
                fechaIngresada = scanner.nextLine();
                desdeFecha = validarFecha(fechaIngresada, formatter);
                if (desdeFecha != null) break; // si fue válida, salgo del bucle
                System.out.println("Fecha inválida. Intente nuevamente.");
            }
        } else {
            System.out.println("Fecha Desde: " + formatter.format(desdeFecha));
        }

        if (hastaFecha == null) {
            while (true) {
                System.out.print("Ingrese Hasta Fecha (dd/MM/aaaa): ");
                fechaIngresada = scanner.nextLine();
                hastaFecha = validarFecha(fechaIngresada, formatter);
                if (hastaFecha != null) break;
                System.out.println("Fecha inválida. Intente nuevamente.");
            }
        } else {
            System.out.println("Hasta Fecha: " + formatter.format(hastaFecha));
        }

        if(validarAmbasFechas(desdeFecha,hastaFecha)){
            System.out.println("FECHAS FINALES:");
            System.out.println("Desde Fecha: "+desdeFecha);
            System.out.println("Hasta Fecha: "+hastaFecha + "\n");
            mostrarEstadoHabitaciones(desdeFecha,hastaFecha); //CU05
        } else{
            clearConsola();
            System.out.println("Error. Usted ha ingresado una fecha final mayor a la inicial.");
            Integer opcion;
            System.out.println("Presione: \n 1.Siguiente. Para volver a ingresar las fechas \n 2.Cancelar. Se retornara a Menu");
            opcion= scanner.nextInt();

            while(opcion!=1 && opcion!=2){
                System.out.println("ERROR: ingrese una opcion valida.");
                System.out.println("Presione: \n 1.Siguiente. Para volver a ingresar las fechas \n 2.Cancelar. Se retornara a Menu");
                opcion= scanner.nextInt();
            }
            if (opcion==1) {
                clearConsola();
                reservarHabitacion();
            } else{
                clearConsola();
                Menu();
            }
        }
        
    }

    public static void mostrarEstadoHabitaciones(Date desdeFecha, Date hastaFecha){
        System.out.println("MOSTRAR ESTADO DE HABITACIONES");        
        gestorHabitacion.muestraEstado();
    }
    public static Date validarFecha(String fechaIngresada, SimpleDateFormat formatter){
        try {
            return formatter.parse(fechaIngresada);
        } catch (ParseException e) {
            return null; // devuelve null si no se pudo parsear
        }
    }
    public static boolean validarAmbasFechas(Date desdeFecha, Date hastaFecha){
        if(desdeFecha.before(hastaFecha)){
            return true;    
        } else {
            return false;
        }
    }

    public static void darBajaHuesped(HuespedDTO huespedDTO){ //el huesped me lo pasa el CU12
        Scanner scanner = new Scanner(System.in);

        /*prueba pero en realidad a dar de baja huesped le llega el huesped del CU10
        String nombre, apellido, tipoDoc, numDoc;
        nombre = scanner.nextLine();
        apellido = scanner.nextLine();
        tipoDoc = scanner.nextLine();
        numDoc = scanner.nextLine();
        HuespedDTO huespedDTO = gestorHuesped.buscarDatos(nombre,apellido,tipoDoc,numDoc);
        */
        int opcion;
        if(huespedDTO.getApellido() == null){System.out.println(" no existe el huesped buscado");}
        else{
            if (!gestorHuesped.seAlojo(huespedDTO)){
                System.out.println("los datos del huesped: "+huespedDTO.getNombre()+" "+huespedDTO.getApellido()+" "+huespedDTO.getTipoDocumento()+" "+huespedDTO.getNumeroDocumento()+" seran eliminados del sistema");
                System.out.println("presione 1 si desea ELIMINAR o 2 si desea CANCELAR");
                opcion = scanner.nextInt();
                if(opcion==1){
                    if(gestorHuesped.eliminarHuesped(huespedDTO)){
                        System.out.println("los datos del huesped: "+huespedDTO.getNombre()+" "+huespedDTO.getApellido()+" "+huespedDTO.getTipoDocumento()+" "+huespedDTO.getNumeroDocumento()+" seran eliminados del sistema");
                    }else {
                        System.out.println("no existe huesped a eliminar");
                    }
                } else if (opcion == 2) {
                    System.out.println("Cancelacion exitosa");
                }
            }else {
                System.out.println(" El huesped no puede ser eliminado pues se ha alojado en el Hotel en alguna oportunidad.");
                System.out.println(" PRESIONE CUALQUIER TECLA PARA CONTINUAR...");
                scanner.nextLine();
            }
        }

    }
}

