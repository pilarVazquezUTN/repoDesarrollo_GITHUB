package Main;
import java.io.IOException;
import java.text.ParseException; 
import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Predicate;

import Classes.Huesped.Huesped;
import Classes.Huesped.HuespedDAO;
import Classes.Huesped.HuespedDTO;
import Classes.Usuario.GestorUsuario;
import Classes.Usuario.UsuarioDAO;
import Classes.Usuario.UsuarioDTO;
import Classes.Habitacion.GestorHabitacion;
import Classes.Huesped.GestorHuesped;
import Classes.Direccion.DireccionDTO;
import Classes.FuncionesUtiles;
import Classes.Validador;
import Classes.DAOFactory;


public class App {
    static GestorHuesped gestorHuesped=new GestorHuesped();
    static GestorUsuario gestorUsuario= new GestorUsuario();
    static GestorHabitacion gestorHabitacion= new GestorHabitacion();
    static FuncionesUtiles funcionesUtiles = new FuncionesUtiles();





    public static void main(String[] args) {
        Bienvenida();
    }

    public static void Bienvenida(){
        Scanner scanner = new Scanner(System.in);
        Integer opcion;

        funcionesUtiles.clearConsola();
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
        System.out.println("4. Autenticarse con otro Usuario");
        System.out.println("5. Salir");
        System.out.print("--- Ingrese una opción: "); 

        ingresaOpcion();

    }

    public static void ingresaOpcion(){
        Scanner scanner = new Scanner(System.in);
        boolean entradaValida=false;
        int opcion= scanner.nextInt();
        funcionesUtiles.clearConsola();
        do{
            if(opcion<1 || opcion>=5){
                funcionesUtiles.clearConsola();
                System.out.println("--------------- Ingrese una opcion correcta!");
                Menu();
            } else{
                entradaValida=true;
            }
        } while(!entradaValida);

        funcionesUtiles.clearConsola();
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
            case 4:
                funcionesUtiles.clearConsola();
                autenticarHuesped();
                break;
            case 5:
                funcionesUtiles.clearConsola();
                System.out.println("Hasta luego!");
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
            funcionesUtiles.clearConsola();
        } while (!contrasenaValida);

        // Creamos el UsuarioDTO para pasarlo al DAO
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre(nombreUsuario);
        usuarioDTO.setContrasena(contrasenaUsuario);

        UsuarioDAO usuarioDAO = (UsuarioDAO) DAOFactory.create(DAOFactory.USUARIO);
        funcionesUtiles.clearConsola();

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
            funcionesUtiles.clearConsola();
            Menu();
        }
    }
    public static HuespedDTO ingresarDatos(Boolean camposVacios, HuespedDTO huespedDTO, DireccionDTO direccionDTO){
        //HuespedDTO huespedDTO = new HuespedDTO();
        Scanner scanner = new Scanner(System.in);
        Date fechaNacDTO;

        if(camposVacios){
            if(huespedDTO.getApellido().isEmpty() || !funcionesUtiles.contieneSoloLetras(huespedDTO.getApellido())){
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
            if(huespedDTO.getNombre().isEmpty() || !funcionesUtiles.contieneSoloLetras(huespedDTO.getNombre())){
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
            if(huespedDTO.getTipoDocumento().isEmpty() || !funcionesUtiles.contieneSoloLetras(huespedDTO.getTipoDocumento())){
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
            if(huespedDTO.getNumeroDocumento().isEmpty() || !funcionesUtiles.contieneSoloNumeros(huespedDTO.getNumeroDocumento())){
                System.out.print("Error en campo. Ingrese CORRECTAMENTE su Numero de Documento: ");
                huespedDTO.setNumeroDocumento(scanner.nextLine().toUpperCase());
            } else{
                System.out.println("Su Numero de Documento: "+huespedDTO.getNumeroDocumento());    
            }
        } else{
            System.out.print("Ingrese su numero de documento: ");
            huespedDTO.setNumeroDocumento(scanner.nextLine());
        } 
        

        String valorCuit= huespedDTO.getCuit();
        if(valorCuit==null){
            System.out.print("Ingrese su CUIT: ");
            huespedDTO.setCuit(scanner.nextLine());
        } else {
            if(!funcionesUtiles.contieneSoloNumeros(huespedDTO.getCuit())){
                //("RESPONSABLE INSCRIPTO".equals(huespedDTO.getCuit()) )
                //System.out.print("Error en campo. Usted es Responsable Inscripto, ingrese un CUIT obligatoraimente: ");
                System.out.print("Error en campo. Ingrese CORRECTAMENTE su CUIT: ");
                huespedDTO.setCuit(scanner.nextLine());
            } else {
                System.out.println("Su CUIT: "+ huespedDTO.getCuit());
            }    
        }
        /* 
        if("RESPONSABLE INSCRIPTO".equals(huespedDTO.getPosicionIva())){
            System.out.print("Error en campo. Usted es Responsable Inscripto, ingrese un CUIT obligatoraimente: ");
            huespedDTO.setCuit(scanner.nextLine());
        } else {
            if(valorCuit!=null){
                System.out.println("Su CUIT: "+ huespedDTO.getCuit());
            } else {
                System.out.print("Ingrese su CUIT: ");
                huespedDTO.setCuit(scanner.nextLine());
            }
        } */
        
        String posIva= huespedDTO.getPosicionIva();
        if(posIva==null){
            System.out.print("Ingrese su Posicion Frente al IVA: ");
            posIva=scanner.nextLine().toUpperCase();
        } else {
            if(huespedDTO.getPosicionIva().isEmpty() || !funcionesUtiles.contieneSoloLetras(huespedDTO.getPosicionIva())){
                System.out.print("Error en campo. Ingrese CORRECTAMENTE su Posicion Frente al IVA: ");
                posIva=scanner.nextLine().toUpperCase();
            } else {
                System.out.println("Su Posicion Frente al IVA: "+huespedDTO.getPosicionIva());    
            }
        }
        if(posIva.isEmpty()){
            posIva="CONSUMIDOR FINAL";
        }
        huespedDTO.setPosicionIva(posIva);

        /* 
        if(camposVacios){
            if(huespedDTO.getPosicionIva().isEmpty() || !funcionesUtiles.contieneSoloLetras(huespedDTO.getPosicionIva())){
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
        */

        if(camposVacios){
            if(huespedDTO.getFechaNacimiento()==null){
                System.out.print("Error en campo. Ingrese CORRECTAMENTE su Fecha de Nacimiento (dd/MM/yyyy): ");

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
                    System.out.println("Su Fecha de Nacimiento: "+funcionesUtiles.convertirDateAString(huespedDTO.getFechaNacimiento()));    
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
            if(direccionDTO.getCalle().isEmpty() || !funcionesUtiles.contieneSoloLetras(direccionDTO.getCalle())){
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
            if(direccionDTO.getNumero().isEmpty() || !funcionesUtiles.contieneSoloNumeros(direccionDTO.getNumero())){
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
            if(direccionDTO.getDepartamento().isEmpty() || !funcionesUtiles.contieneSoloLetras(direccionDTO.getDepartamento())){
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
            if(direccionDTO.getPiso().isEmpty() || !funcionesUtiles.contieneSoloNumeros(direccionDTO.getPiso())){
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
            if(direccionDTO.getCodigoPostal().isEmpty() || !funcionesUtiles.contieneSoloNumeros(direccionDTO.getCodigoPostal())){
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
            if(direccionDTO.getLocalidad().isEmpty() || !funcionesUtiles.contieneSoloLetras(direccionDTO.getLocalidad())){
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
            if(direccionDTO.getProvincia().isEmpty() || !funcionesUtiles.contieneSoloLetras(direccionDTO.getProvincia())){
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
            if(direccionDTO.getPais().isEmpty() || !funcionesUtiles.contieneSoloLetras(direccionDTO.getPais())){
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
        
        String telefonoHuesped = huespedDTO.getTelefono();
        if(telefonoHuesped==null){
            System.out.print("Ingrese su Telefono: ");
            huespedDTO.setTelefono(scanner.nextLine());
        } else{
            if(huespedDTO.getTelefono().isEmpty() || !funcionesUtiles.contieneSoloNumeros(huespedDTO.getTelefono())){
                System.out.print("Error en campo. Ingrese CORRECTAMENTE su Telefono: ");
                huespedDTO.setTelefono(scanner.nextLine());
            } else{
                System.out.println("Su Telefono: "+huespedDTO.getTelefono());    
            }
        }
        /*
            if(camposVacios){
            if(telefonoHuesped==null || !funcionesUtiles.contieneSoloNumeros(huespedDTO.getTelefono())){
                System.out.print("Error en campo. Ingrese CORRECTAMENTE su Telefono: ");
                huespedDTO.setTelefono(scanner.nextLine());
            } else{
                System.out.println("Su Telefono: "+huespedDTO.getTelefono());    
            }
            } else{
                System.out.print("Ingrese su Telefono: ");
                huespedDTO.setTelefono(scanner.nextLine());
            } 
         */
         
        
        String emailHuesped = huespedDTO.getEmail(); 
        if(emailHuesped==null){
            System.out.print("Ingrese su email: ");
            huespedDTO.setEmail(scanner.nextLine().toUpperCase());
        } else{
            if(funcionesUtiles.emailValido(emailHuesped)){
                System.out.println("Su email: "+huespedDTO.getEmail());
            } else{
                System.out.print("Error en campo. Ingrese CORRECTAMENTE su Email: ");
                huespedDTO.setEmail(scanner.nextLine());
            }
            
        }
        /*
        if(!huespedDTO.getEmail().isEmpty()){
            System.out.println("Su email: "+huespedDTO.getEmail());
        } else {
            System.out.print("Ingrese su email: ");
            huespedDTO.setEmail(scanner.nextLine().toUpperCase());
        }
         */
        
        
        
        if(camposVacios){
            if(huespedDTO.getOcupacion().isEmpty() || !funcionesUtiles.contieneSoloLetras(huespedDTO.getOcupacion())){
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
            if(huespedDTO.getNacionalidad().isEmpty() || !funcionesUtiles.contieneSoloLetras(huespedDTO.getNacionalidad())){
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

        Integer opcion;
        opcion =scanner.nextInt();

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
                funcionesUtiles.clearConsola();
                Menu();
            } else{
                //sistema vuelve al paso1: muestra todos los datos nuevamente
                huespedDTO=null;
                funcionesUtiles.clearConsola();
                darAltaHuesped();
            }
        }

        return huespedDTO; //FALTA VER QUE PASA SI PRESIONA CANCELAR   
    }
    public static void controlarCampos(HuespedDTO huespedDTO, DireccionDTO direccionDTO){
        //veo si algun campo obligatorio esta sin completar
        Scanner scanner = new Scanner(System.in);
        funcionesUtiles.clearConsola();

        if(huespedDTO.getNombre().isEmpty() || huespedDTO.getApellido().isEmpty() || huespedDTO.getTipoDocumento().isEmpty() || 
        huespedDTO.getNumeroDocumento().isEmpty() || (huespedDTO.getDireccionHuesped()).getCalle().isEmpty() || 
        (huespedDTO.getDireccionHuesped()).getNumero().isEmpty() || (huespedDTO.getDireccionHuesped()).getDepartamento().isEmpty() || 
        (huespedDTO.getDireccionHuesped()).getPiso().isEmpty() || (huespedDTO.getDireccionHuesped()).getCodigoPostal().isEmpty() || 
        (huespedDTO.getDireccionHuesped()).getLocalidad().isEmpty() || (huespedDTO.getDireccionHuesped()).getProvincia().isEmpty() ||
        (huespedDTO.getDireccionHuesped()).getPais().isEmpty() || huespedDTO.getTelefono().isEmpty() || 
        huespedDTO.getOcupacion().isEmpty() || huespedDTO.getNacionalidad().isEmpty() || huespedDTO.getFechaNacimiento()==null ||
        !funcionesUtiles.contieneSoloLetras(huespedDTO.getNombre()) || !funcionesUtiles.contieneSoloLetras(huespedDTO.getApellido()) ||
        !funcionesUtiles.contieneSoloLetras(huespedDTO.getTipoDocumento()) || !funcionesUtiles.contieneSoloNumeros(huespedDTO.getNumeroDocumento()) || 
        !funcionesUtiles.contieneSoloLetras(huespedDTO.getDireccionHuesped().getCalle()) || 
        !funcionesUtiles.contieneSoloNumeros(huespedDTO.getDireccionHuesped().getNumero()) ||
        !funcionesUtiles.contieneSoloLetras(huespedDTO.getDireccionHuesped().getDepartamento()) ||
        !funcionesUtiles.contieneSoloNumeros(huespedDTO.getDireccionHuesped().getPiso()) ||
        !funcionesUtiles.contieneSoloNumeros(huespedDTO.getDireccionHuesped().getCodigoPostal()) || 
        !funcionesUtiles.contieneSoloLetras(huespedDTO.getDireccionHuesped().getLocalidad()) || 
        !funcionesUtiles.contieneSoloLetras(huespedDTO.getDireccionHuesped().getProvincia()) || 
        !funcionesUtiles.contieneSoloLetras(huespedDTO.getDireccionHuesped().getPais()) || 
        !funcionesUtiles.contieneSoloNumeros(huespedDTO.getTelefono()) || !funcionesUtiles.contieneSoloLetras(huespedDTO.getOcupacion()) || 
        !funcionesUtiles.contieneSoloLetras(huespedDTO.getNacionalidad()) || 
        !funcionesUtiles.contieneSoloNumeros(huespedDTO.getCuit()) ||
        !funcionesUtiles.emailValido(huespedDTO.getEmail()) ){

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


    public static void buscarHuesped(){
        /*se presenta pantalla para buscar huesped 
         ingresa nombre, apellido, tipo doc, num doc
         mostrar error de algun dato invalido
         sistema blanquea campos cuando esten OK
         se vuelve al Menu
        */
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese apellido: ");
        String apellidoHuesped = scanner.nextLine();
        if(!funcionesUtiles.contieneSoloLetras(apellidoHuesped) && !apellidoHuesped.isEmpty()){
            //funcionesUtiles.clearConsola();
            System.out.print("Ingrese apellido valido: ");
            apellidoHuesped = scanner.nextLine();
        }
        System.out.print("Ingrese nombre: ");
        String nombreHuesped = scanner.nextLine();
        if(!funcionesUtiles.contieneSoloLetras(nombreHuesped) && !nombreHuesped.isEmpty()){
            //funcionesUtiles.clearConsola();
            System.out.print("Ingrese nombre valido: ");
            nombreHuesped = scanner.nextLine();
        }
        System.out.print("Ingrese Tipo de documento: ");
        String tipoDoc = scanner.nextLine();
        if(!funcionesUtiles.contieneSoloLetras(tipoDoc) && !tipoDoc.isEmpty()){
            //funcionesUtiles.clearConsola();
            System.out.print("Ingrese tipo de documento valido: ");
            tipoDoc = scanner.nextLine();
        }
        System.out.print("Ingrese su documento: ");
        String numDoc = scanner.nextLine();
        if(!funcionesUtiles.contieneSoloNumeros(numDoc) &&  !numDoc.isEmpty()){
            //funcionesUtiles.clearConsola();
            System.out.print("Ingrese numero de documento valido: ");
            numDoc = scanner.nextLine();
        }
        HuespedDAO huespedDAO = (HuespedDAO) DAOFactory.create(DAOFactory.HUESPED);
        HuespedDTO huespedDTO = new HuespedDTO();
        huespedDTO=gestorHuesped.buscarDatos(nombreHuesped,apellidoHuesped,tipoDoc,numDoc);
        funcionesUtiles.clearConsola();
        if(huespedDTO.getApellido()!=null){
            System.out.println("Huesped seleccionado: ");
            System.out.println("  Nombre: " + huespedDTO.getNombre());
            System.out.println("  Apellido: " + huespedDTO.getApellido());
            System.out.println("  Tipo documento: " + huespedDTO.getTipoDocumento());
            System.out.println("  N° documento: " + huespedDTO.getNumeroDocumento());
            System.out.println("DESEA MODIFICAR EL HUESPED? - indique SI o NO ");
            Scanner sc = new Scanner(System.in);
            String op = sc.nextLine().trim();
            while (!(op.equalsIgnoreCase("si") || op.equalsIgnoreCase("no"))) {

                System.out.println("Indique - SI o NO");
                op = sc.nextLine().trim();
            }
            if ( op.equals("si")){
                funcionesUtiles.clearConsola();
                modificarHuesped1(huespedDTO,gestorHuesped); //aca llamo a modificar huesped1 con Huesped DTO Y HuespedDto debe tener todos los campos

            }
        }
        funcionesUtiles.clearConsola();
        Menu();
        //nose si deberia llamar al gestor o a la clase
        //System.out.println("se elimino: " + gestorHuesped.eliminarHuesped(huespedDTO)); es para probar a ver si elimina pero necesito el CU 12 PARA
    }




    public static void modificarHuesped(Map<String, String> campos,HuespedDTO huespedDTO, GestorHuesped gestorHuesped, Map<String, Predicate<String>> validadores,
                                        Set<String> noObligatorios, HuespedDTO huespedDNI, String dniNOMod, String tipoNomod ) {
        Scanner sc = new Scanner(System.in);
        //muestraCamposIngresados(campos);
        // valida campos, pide incorrectos
        System.out.println("PARA ACEPTAR PRESIONE 1: ");
        System.out.println("PARA CANCELAR PRESIONE 2: ");
        System.out.println("PARA BORRAR PRESIONE 3: ");
        int opcion = sc.nextInt();

        if ( opcion == 1) {  //si la opcion es 1 acepta los cambios , entonces valida - SI NO VALIDA EL CMAPO INGRESADO
            opcionAceptar(campos,validadores, noObligatorios,huespedDNI,dniNOMod,tipoNomod,huespedDTO);
        }
        //cancelar
        else if (opcion ==2){
            opcionCancelar(campos,huespedDTO,gestorHuesped,validadores,noObligatorios,huespedDNI,dniNOMod,tipoNomod);
        }

        else if (opcion == 3){
            darBajaHuesped(huespedDTO);
        }

        /*System.out.println("\n Todos los campos válidos:"); //key - value
        campos.forEach((k, v) -> System.out.println(k + ": " + v));

        System.out.println("la operacion a culminado con exito ");

        //ahora llamo a geestor para q llame al dao y guarde en el archivo, le paso el dni ue esta en el archivo para poder buscar
        gestorHuesped.modificarDatosHuespedArchivo(huespedDTO, "infoBuscarHuespedes.txt",huespedDTO.getDireccionHuesped(), tipoNomod, dniNOMod );

        //huespedDAO.actualizarHuesped(rutaArchivo, huespedDTO, huespedDTO.getDireccionHuesped(), tipoDoc, dninoMod);
*/


    }


    public static void modificarHuesped1 (HuespedDTO huespedDTO, GestorHuesped gestorHuesped) {
        String dniNOMod = huespedDTO.getNumeroDocumento(); //son los no modficados asi dsp busco en q parte de el archivo esta
        String tipoNomod=huespedDTO.getTipoDocumento();

        Scanner sc = new Scanner(System.in);
        HuespedDTO huespedDNI = new HuespedDTO();


        Map<String, String> campos = new LinkedHashMap<>();  //map es linkedHASHMAP ENTONCES LO QUE HACE ES CUANDO LE INSERTO LOS VALORES D ELOS CAMPOS LOS MANTINEE ASI
        Map<String, Predicate<String>> validadores = new HashMap<>();

        Set<String> noObligatorios = Set.of("CUIT", "email");

        //se cargan los datos
        gestorHuesped.modificarHuespedGestor(huespedDTO, huespedDTO.getDireccionHuesped(), "infoBuscarHuespedes.csv", campos, validadores);//lama para tener los datos cargaods

        validacionyOpciones(campos,huespedDNI,noObligatorios,huespedDTO,tipoNomod,dniNOMod); //llama a pedir los datos puedo llamar ahi primero y dsp llamar a esta funcon
        modificarHuesped(campos,huespedDTO,gestorHuesped,validadores,noObligatorios,huespedDNI,dniNOMod,tipoNomod);

    System.out.println("DESEA REALIZAR OTRA OPERACION? - indique SI o NO ");
    Scanner sc2 = new Scanner(System.in);
    String op = sc2.nextLine().trim();

        while (!(op.equalsIgnoreCase("si") || op.equalsIgnoreCase("no"))) {

            System.out.println("Indique - SI o NO");
            op = sc.nextLine().trim();
        }

    if( sc.nextLine().equals("si")){
        System.out.println("\n ");
        Menu();
    }






    }




    public static void validacionyOpciones( Map<String, String> campos,HuespedDTO huespedDNI, Set<String> noObligatorios, HuespedDTO huespedDTO, String tipoNomod, String dniNomod){

        Scanner sc = new Scanner(System.in);

        for (String campo : campos.keySet()) {  // for primeoro para apellido, nombre , asi , cada campo en el keyset
            boolean ingresado = false;

            while (!ingresado) { //aca el while para verificar cada campo y q vuelva a ingresar valor

                System.out.println(campo + ": " + campos.get(campo));
                System.out.print("Ingrese nuevo valor (Enter = mantener, b = borrar): ");
                String input = sc.nextLine().trim();

                //para que se guarden en mayuscula si ingresa un dato que sea String
                if(!campo.equals("numeroDocumento") || !campo.equals("CUIT") || !campo.equals("fechaNacimiento") || 
                    !campo.equals("numero") || !campo.equals("piso") || !campo.equals("codigoPostal") || 
                    !campo.equals("telefono")){
                    input=input.toUpperCase();
                }

                if (input.isEmpty()) {
                    // Enter se mantiene valor --para el caso
                    ingresado = true;
                    if ( campo.equals("tipoDocumento")){
                        huespedDNI.setTipoDocumento(huespedDTO.getTipoDocumento());
                    }


                }
                else if (input.equalsIgnoreCase("b")) {
                    // Quiere borrar - si es obligatorio no puede, si no esta en NO OBLIGATORIOS ES PORQ ES OBLIGATORIO
                    if (!noObligatorios.contains(campo)) {
                        System.out.println("\n campo obligatorio no puede quedar vacio ");
                    } else {
                        campos.put(campo, ""); // borrar valor
                        ingresado = true;
                    }
                }
                else {
                    // Nuevo valor ingresado
                    campos.put(campo, input);

                    System.out.println(campo + ": " + input);
                    if ( campo.equals("tipoDocumento" ) && !input.equalsIgnoreCase(tipoNomod)){
                        huespedDNI.setTipoDocumento(input.toUpperCase());

                    }
                    else if (campo.equals("numeroDocumento") && !input.equalsIgnoreCase(dniNomod)){
                        huespedDNI.setNumeroDocumento(input);
                    }
                    ingresado = true;
                }
            }
        }

    }

   /* public static void muestraCamposIngresados(Map<String, String> campos){
        for (Map.Entry<String, String> entry : campos.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
*/
    public static void opcionAceptar(Map<String, String> campos,Map<String, Predicate<String>> validadores,
                                     Set<String> noObligatorios, HuespedDTO huespedDNI,String  dniNOMod,String tipoNomod, HuespedDTO huespedDTO ){
        Scanner sc = new Scanner(System.in);

        //VUELVE A PEDIR DATOS Y VUELVE A VALIDAR



        boolean todosValidos;

        do {
            todosValidos = true;

            for (Map.Entry<String, String> entry : campos.entrySet()) {
                String campo = entry.getKey();
                String valor = entry.getValue();

                Predicate<String> validador = validadores.get(campo);
                // Si es obligatorio y vacío .inválido


                if ((!noObligatorios.contains(campo) && valor.isBlank()) || !validador.test(valor)) {

                    System.out.println("\n valor inválido para '" + campo + "': " + valor);

                    boolean ingresado = false;

                    while (!ingresado) {
                        System.out.print("ingrese " + campo + " nuevamente - (B para borrar): ");
                        String input = sc.nextLine().trim();

                        if (input.isEmpty() && noObligatorios.contains(campo)) {
                            // Enter en opcional, se puede  mantener vacío
                            ingresado = true;

                        }
                        else if (input.equalsIgnoreCase("b")) {
                            if (!noObligatorios.contains(campo)) {
                                System.out.println("Este campo es obligatorio y no puede quedar vacío.");
                            } else {
                                campos.put(campo, "");
                                ingresado = true;
                            }
                        }
                        else {
                            campos.put(campo, input);
                            if ( campo.equals("tipoDocumento" ) && !input.equalsIgnoreCase(tipoNomod)){
                                huespedDNI.setTipoDocumento(input);

                            }
                            else if (campo.equals("numeroDocumento") && !input.equalsIgnoreCase(dniNOMod)){
                                huespedDNI.setNumeroDocumento(input);
                            }
                            ingresado = true;
                        }
                    }
                    todosValidos = false;
                }
            }
        } while (!todosValidos);





        //entra a modificar otro huesped en el caso q hayan ingresadp un dni nuevo, en otro caso entra al else
        if (huespedDNI.getNumeroDocumento() != null && huespedDNI.getTipoDocumento()!=null && !huespedDNI.getNumeroDocumento().equalsIgnoreCase(dniNOMod) ) {//si es distinto de null es porq ingreso otro dni

            if (gestorHuesped.chequearExisteHuesped(huespedDNI)) {  //llama al gestor q verifique si ya esta el doc
                System.out.println("¡CUIDADO NUMERO DOCUMENTO YA EXISTE EN EL SISTEMA");
                System.out.println("1. Aceptar igualmente");
                System.out.println("2. Corregir");

                int entrada = sc.nextInt();


                while ((entrada != 1 && (entrada != 2))) {

                    System.out.println("Entrada inválida. Debe ingresar 1 o 2.");
                    System.out.println("1. Aceptar igualmente");
                    System.out.println("2. Corregir");

                    entrada = Integer.parseInt(sc.nextLine().trim());

                }

                if (entrada != 1) {
                    //pide datos y se vuelven a ingresar
                    System.out.println("corregir datos tipo y numero de documento");
                    System.out.println("Ingrese el tipo");
                    System.out.println("Ingrese el dni");
                    String tipoDoc = sc.nextLine().trim();
                    String valorDoc = sc.nextLine().trim();

                    while (!(Validador.esNumeroValido.test(valorDoc)) && !(Validador.esStringValido.test(tipoDoc))) {  //PUEDE FALLAR
                        System.out.println("INGRESE VALORES VALIDOS");
                        //tenog q validar d enuevo
                        System.out.println("Ingrese el tipo");
                        tipoDoc = sc.nextLine().trim();

                        System.out.println("Ingrese el dni");
                        valorDoc = sc.nextLine().trim();
                    }
                    campos.put("tipoDocumento", valorDoc);
                    campos.put("numeroDocumento", valorDoc);
                    modificarHuesped(campos,huespedDTO,gestorHuesped,validadores,noObligatorios,huespedDNI,dniNOMod,tipoNomod);
                }
                else {
                    //opta por aceptar igualmente
                    System.out.println("la operacion a culminado con exito ");

                    //ahora llamo a geestor para q llame al dao y guarde en el archivo, le paso el dni ue esta en el archivo para poder buscar


                    System.out.println(huespedDNI.getTipoDocumento());
                    System.out.println(huespedDNI.getNumeroDocumento());


                    gestorHuesped.modificarDatosHuespedArchivo(campos,huespedDTO, "infoBuscarHuespedes.csv", huespedDTO.getDireccionHuesped(), huespedDNI.getTipoDocumento(), huespedDNI.getNumeroDocumento());
                }

            }} //si huespedDni es null es porq quiere modificar datos de el huesped q esta
        else{  //si no existe el dni, es porq le quiere cambiar a ese q ya esta el dni ENTONCES OCUPA EL DNI VIEJO

            System.out.println("\n Todos los campos válidos:"); //key - value
            campos.forEach((k, v) -> System.out.println(k + ": " + v));

            System.out.println("la operacion a culminado con exito ");

            //ahora llamo a geestor para q llame al dao y guarde en el archivo, le paso el dni ue esta en el archivo para poder buscar
            gestorHuesped.modificarDatosHuespedArchivo(campos, huespedDTO, "infoBuscarHuespedes.csv",huespedDTO.getDireccionHuesped(), tipoNomod, dniNOMod );

            //huespedDAO.actualizarHuesped(rutaArchivo, huespedDTO, huespedDTO.getDireccionHuesped(), tipoDoc, dninoMod);
        }


    }

    public static void opcionCancelar(Map<String, String> campos,HuespedDTO huespedDTO, GestorHuesped gestorHuesped, Map<String, Predicate<String>> validadores,
                                      Set<String> noObligatorios, HuespedDTO huespedDNI, String dniNOMod, String tipoNomod )  {
        Scanner sc = new Scanner(System.in);
        System.out.println("¿Desea cancelar la modificacion del huesped?");
        System.out.println("Indique - SI o NO");
        String op = sc.nextLine().trim();
        while (!(op.equalsIgnoreCase("si") || op.equalsIgnoreCase("no"))) {

           System.out.println("Indique - SI o NO");
            op = sc.nextLine().trim();
        }
        if (op.equalsIgnoreCase("si")){
            System.out.println("Se cancela la modificacin del huesped");
            Menu();
        }
        else{ //vuelve a mostrar hasta el boton aceptar borrar cancelar
            modificarHuesped(campos, huespedDTO,gestorHuesped,validadores,noObligatorios,huespedDNI,dniNOMod,tipoNomod);

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
            funcionesUtiles.clearConsola();
            System.out.println("FECHAS SELECCIONADAS: "+funcionesUtiles.convertirDateAString(desdeFecha)+" hasta el "+funcionesUtiles.convertirDateAString(hastaFecha)+"\n");
            //System.out.println("Desde Fecha: "+desdeFecha);
            //System.out.println("Hasta Fecha: "+hastaFecha + "\n");
            mostrarEstadoHabitaciones(desdeFecha,hastaFecha); //CU05
        } else{
            funcionesUtiles.clearConsola();
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
                funcionesUtiles.clearConsola();
                reservarHabitacion();
            } else{
                funcionesUtiles.clearConsola();
                Menu();
            }
        }
        
    }

    public static void mostrarEstadoHabitaciones(Date desdeFecha, Date hastaFecha){
        //funcionesUtiles.clearConsola();
        System.out.println("MOSTRAR ESTADO DE HABITACIONES");        
        gestorHabitacion.muestraEstado(desdeFecha,hastaFecha);
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

    public static void clearConsola() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException ex) {
            System.out.println("No se pudo limpiar la consola.");
        }
    }


    public static int terminar(){
        return 0;
    }

}

