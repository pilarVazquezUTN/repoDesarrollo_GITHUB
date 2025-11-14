package Main;

import java.io.IOException;
import java.text.ParseException; 
import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Predicate;

import Classes.Huesped.HuespedDTO;
import Classes.Reserva.GestorReservaInterfaz;
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

    /**
     * Bienvenida, funcion q llama a autenticar huesped
     */
    public static void Bienvenida(){
        Scanner scanner = new Scanner(System.in);
        String opcion;

        FuncionesUtiles.clearConsola();
        do{
            System.out.println("BIENVENIDO A LA GESTION DEL HOTEL. \n Presione 1 para Autenticar su Usuario. ");
            opcion=scanner.nextLine();
        } while (!opcion.equalsIgnoreCase("1"));

        autenticarHuesped();

    }


    /**
     * Menu tiene las opciones que puede realiazar el usuario
     */
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
        //boolean entradaValida=false;
        int opcion= scanner.nextInt();
        FuncionesUtiles.clearConsola();
        if(opcion<1 || opcion>5){
            FuncionesUtiles.clearConsola();
            System.out.println("--------------- Ingrese una opcion correcta!");
            Menu();
        }

        FuncionesUtiles.clearConsola();
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
                FuncionesUtiles.clearConsola();
                autenticarHuesped();
                break;
            case 5:
                FuncionesUtiles.clearConsola();
                System.out.println("Hasta luego!");
                break;
        }
    }

    /**
     * Permite autenticar a un huesped en el sistema.
     *
     * Solicita el nombre de usuario y la contrasena por consola.
     * La contrasena se ingresa de forma oculta y se valida antes de continuar.
     * Si los datos son correctos, se concede el acceso y se muestra el menu principal.
     * Si el usuario no existe o la contrasena es incorrecta, se vuelve a pedir la autenticacion.
     */


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
            FuncionesUtiles.clearConsola();
        } while (!contrasenaValida);

        // Creamos el UsuarioDTO para pasarlo al DAO
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre(nombreUsuario);
        usuarioDTO.setContrasena(contrasenaUsuario);

        UsuarioDAO usuarioDAO = (UsuarioDAO) DAOFactory.create(DAOFactory.USUARIO);
        FuncionesUtiles.clearConsola();

        // Llamamos al Gestor
        if (gestorUsuario.autenticarUsuario(usuarioDAO, usuarioDTO)) {
            System.out.println("Usuario Encontrado. Acceso concedido.\n");

            Menu();
           // cancelarReserva(); //ESTO ES DE PRUEBA!!!!!!!!!!!!!!!!!!!!!!!!!
        } else {
            System.out.println("Usuario no encontrado. Se vuelve a Autenticación de Usuario.\n");
            autenticarHuesped();
        }
    }
    /**
     * Verifica si una contrasena cumple con las reglas de seguridad definidas.
     * @param contrasena texto a validar
     * @return true si la contrasena cumple con todas las condiciones, false en caso contrario
     */

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



    /**
 * Lee una contrasena desde consola sin mostrar los caracteres ingresados.
 * Cada caracter se reemplaza por un asterisco (*) mientras se escribe.
 *
 * @return la contrasena ingresada por el usuario
 */
public static String leerContrasenaOculta() {
    StringBuilder contrasena = new StringBuilder();
    try {
        int caracter;
        while ((caracter = System.in.read()) != '\n' && caracter != '\r') { // hasta Enter
            if (caracter == '\b') { // backspace: borrar el ultimo caracter
                if (contrasena.length() > 0) {
                    contrasena.deleteCharAt(contrasena.length() - 1);
                    System.out.print("\b \b");
                }
            } else {
                contrasena.append((char) caracter);
                System.out.print("*");
            }
        }
        System.out.println();
    } catch (IOException e) {
        System.err.println("Error al leer la contrasena: " + e.getMessage());
    }
    return contrasena.toString();
}

    /**
     * Da de alta a un nuevo huesped en el sistema.
     *
     * El metodo solicita los datos personales y de direccion del huesped,
     * valida la informacion y registra el nuevo huesped mediante el gestor.
     * Al finalizar, muestra un mensaje de confirmacion y pregunta si se desea
     * cargar otro registro. Si el usuario elige "si", el proceso se repite;
     * en caso contrario, limpia la consola y vuelve al menu principal.
     */
    public static void darAltaHuesped() {
        Scanner scanner = new Scanner(System.in);
        HuespedDTO huespedDTO = new HuespedDTO();
        Boolean camposVacios = false; // controla campos mal ingresados despues del primer intento
        DireccionDTO direccionDTO = new DireccionDTO();

        ingresarDatos(camposVacios, huespedDTO, direccionDTO);
        gestorHuesped.registrarHuesped(huespedDTO);

        Integer opcion;
        do {
            System.out.println("El huesped " + huespedDTO.getNombre() + "," + huespedDTO.getApellido()
                    + " ha sido satisfactoriamente cargado al sistema. \n¿Desea cargar otro?\n"
                    + "1. si \n2. no");

            opcion = scanner.nextInt();
        } while (opcion != 1 && opcion != 2);

        if (opcion == 1) {
            // vuelve a cargar mas huespedes
            darAltaHuesped();
        } else {
            FuncionesUtiles.clearConsola();
            Menu();
        }
    }


    /**
     * Ingreso de todos los datos del huesped, validacion correspondiente
     * @param camposVacios
     * @param huespedDTO datos del huesped
     * @param direccionDTO direccion
     * @return
     */
    public static HuespedDTO ingresarDatos(Boolean camposVacios, HuespedDTO huespedDTO, DireccionDTO direccionDTO){
        //HuespedDTO huespedDTO = new HuespedDTO();
        Scanner scanner = new Scanner(System.in);
        Date fechaNacDTO;

        if(camposVacios){
            if(huespedDTO.getApellido().isEmpty() || !FuncionesUtiles.contieneSoloLetras(huespedDTO.getApellido())){
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
            if(huespedDTO.getNombre().isEmpty() || !FuncionesUtiles.contieneSoloLetras(huespedDTO.getNombre())){
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
            if(huespedDTO.getTipoDocumento().isEmpty() || !FuncionesUtiles.contieneSoloLetras(huespedDTO.getTipoDocumento())){
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
            if(huespedDTO.getNumeroDocumento().isEmpty() || !FuncionesUtiles.contieneSoloNumeros(huespedDTO.getNumeroDocumento())){
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
            if(!FuncionesUtiles.contieneSoloNumeros(huespedDTO.getCuit())){
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
            if(huespedDTO.getPosicionIva().isEmpty() || !FuncionesUtiles.contieneSoloLetras(huespedDTO.getPosicionIva())){
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
                    System.out.println("Su Fecha de Nacimiento: "+FuncionesUtiles.convertirDateAString(huespedDTO.getFechaNacimiento()));    
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
            if(direccionDTO.getCalle().isEmpty() || !FuncionesUtiles.contieneSoloLetras(direccionDTO.getCalle())){
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
            if(direccionDTO.getNumero().isEmpty() || !FuncionesUtiles.contieneSoloNumeros(direccionDTO.getNumero())){
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
            if(direccionDTO.getDepartamento().isEmpty() || !FuncionesUtiles.contieneSoloLetras(direccionDTO.getDepartamento())){
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
            if(direccionDTO.getPiso().isEmpty() || !FuncionesUtiles.contieneSoloNumeros(direccionDTO.getPiso())){
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
            if(direccionDTO.getCodigoPostal().isEmpty() || !FuncionesUtiles.contieneSoloNumeros(direccionDTO.getCodigoPostal())){
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
            if(direccionDTO.getLocalidad().isEmpty() || !FuncionesUtiles.contieneSoloLetras(direccionDTO.getLocalidad())){
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
            if(direccionDTO.getProvincia().isEmpty() || !FuncionesUtiles.contieneSoloLetras(direccionDTO.getProvincia())){
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
            if(direccionDTO.getPais().isEmpty() || !FuncionesUtiles.contieneSoloLetras(direccionDTO.getPais())){
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
            if(huespedDTO.getTelefono().isEmpty() || !FuncionesUtiles.contieneSoloNumeros(huespedDTO.getTelefono())){
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
            if(FuncionesUtiles.emailValido(emailHuesped)){
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
            if(huespedDTO.getOcupacion().isEmpty() || !FuncionesUtiles.contieneSoloLetras(huespedDTO.getOcupacion())){
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
            if(huespedDTO.getNacionalidad().isEmpty() || !FuncionesUtiles.contieneSoloLetras(huespedDTO.getNacionalidad())){
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
                FuncionesUtiles.clearConsola();
                Menu();
            } else{
                //sistema vuelve al paso1: muestra todos los datos nuevamente
                huespedDTO=null;
                FuncionesUtiles.clearConsola();
                darAltaHuesped();
            }
        }

        return huespedDTO; //FALTA VER QUE PASA SI PRESIONA CANCELAR   
    }

    /**
     * se controlan los campos del huesped
     * @param huespedDTO
     * @param direccionDTO
     */
    public static void controlarCampos(HuespedDTO huespedDTO, DireccionDTO direccionDTO){
        //veo si algun campo obligatorio esta sin completar
        Scanner scanner = new Scanner(System.in);
        FuncionesUtiles.clearConsola();

        if(huespedDTO.getNombre().isEmpty() || huespedDTO.getApellido().isEmpty() || huespedDTO.getTipoDocumento().isEmpty() || 
        huespedDTO.getNumeroDocumento().isEmpty() || (huespedDTO.getDireccionHuesped()).getCalle().isEmpty() || 
        (huespedDTO.getDireccionHuesped()).getNumero().isEmpty() || (huespedDTO.getDireccionHuesped()).getDepartamento().isEmpty() || 
        (huespedDTO.getDireccionHuesped()).getPiso().isEmpty() || (huespedDTO.getDireccionHuesped()).getCodigoPostal().isEmpty() || 
        (huespedDTO.getDireccionHuesped()).getLocalidad().isEmpty() || (huespedDTO.getDireccionHuesped()).getProvincia().isEmpty() ||
        (huespedDTO.getDireccionHuesped()).getPais().isEmpty() || huespedDTO.getTelefono().isEmpty() || 
        huespedDTO.getOcupacion().isEmpty() || huespedDTO.getNacionalidad().isEmpty() || huespedDTO.getFechaNacimiento()==null ||
        !FuncionesUtiles.contieneSoloLetras(huespedDTO.getNombre()) || !FuncionesUtiles.contieneSoloLetras(huespedDTO.getApellido()) ||
        !FuncionesUtiles.contieneSoloLetras(huespedDTO.getTipoDocumento()) || !FuncionesUtiles.contieneSoloNumeros(huespedDTO.getNumeroDocumento()) || 
        !FuncionesUtiles.contieneSoloLetras(huespedDTO.getDireccionHuesped().getCalle()) || 
        !FuncionesUtiles.contieneSoloNumeros(huespedDTO.getDireccionHuesped().getNumero()) ||
        !FuncionesUtiles.contieneSoloLetras(huespedDTO.getDireccionHuesped().getDepartamento()) ||
        !FuncionesUtiles.contieneSoloNumeros(huespedDTO.getDireccionHuesped().getPiso()) ||
        !FuncionesUtiles.contieneSoloNumeros(huespedDTO.getDireccionHuesped().getCodigoPostal()) || 
        !FuncionesUtiles.contieneSoloLetras(huespedDTO.getDireccionHuesped().getLocalidad()) || 
        !FuncionesUtiles.contieneSoloLetras(huespedDTO.getDireccionHuesped().getProvincia()) || 
        !FuncionesUtiles.contieneSoloLetras(huespedDTO.getDireccionHuesped().getPais()) || 
        !FuncionesUtiles.contieneSoloNumeros(huespedDTO.getTelefono()) || !FuncionesUtiles.contieneSoloLetras(huespedDTO.getOcupacion()) || 
        !FuncionesUtiles.contieneSoloLetras(huespedDTO.getNacionalidad()) || 
        !FuncionesUtiles.contieneSoloNumeros(huespedDTO.getCuit()) ||
        !FuncionesUtiles.emailValido(huespedDTO.getEmail()) ){

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

    /**
     * verificacion del documento
     * @param huespedDTO datos del huesped
     * @return
     */
    public static boolean verificarDocumento(HuespedDTO huespedDTO){
        return gestorHuesped.verificarDocumento(huespedDTO);
    }

    /**
     * se registra un huesped y su direccion
     * @param huespedDTO
     * @param direccionDTO
     */

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

    /**
     * Permite buscar huespedes en el sistema segun los datos ingresados por consola.
     *
     * El metodo pide al usuario que ingrese uno o varios criterios de busqueda:
     * apellido, nombre, tipo de documento y numero de documento.
     * Cada campo puede dejarse vacio si no se desea filtrar por ese dato.
     *
     * Comportamiento:
     * - Valida que los datos ingresados sean correctos (solo letras o solo numeros segun corresponda).
     * - Llama al gestor de huespedes para realizar la busqueda segun los criterios ingresados.
     * - Si no se ingresa ningun dato, se muestran todos los huespedes cargados.
     * - Si se ingresa algun filtro, se muestran solo los huespedes que cumplan con esos datos.
     * - Si no se encuentra ninguno, se informa por consola.
     * - Si hay resultados, el usuario puede elegir si quiere modificar alguno indicando su numero.
     *
     * Si se selecciona un huesped valido, se llama al metodo modificarHuesped del gestor.
     *
     * @return No devuelve ningun valor y toda la interaccion se realiza por consola.
     */
    public static void buscarHuesped(){
        
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese apellido: ");
        String apellidoHuesped = scanner.nextLine();
        while(!FuncionesUtiles.contieneSoloLetras(apellidoHuesped) && !apellidoHuesped.isEmpty()){
            //funcionesUtiles.clearConsola();
            System.out.print("Ingrese apellido valido: ");
            apellidoHuesped = scanner.nextLine();
        }
        System.out.print("Ingrese nombre: ");
        String nombreHuesped = scanner.nextLine();
        while(!FuncionesUtiles.contieneSoloLetras(nombreHuesped) && !nombreHuesped.isEmpty()){
            //funcionesUtiles.clearConsola();
            System.out.print("Ingrese nombre valido: ");
            nombreHuesped = scanner.nextLine();
        }
        System.out.print("Ingrese Tipo de documento: ");
        String tipoDoc = scanner.nextLine();
        while(!FuncionesUtiles.contieneSoloLetras(tipoDoc) && !tipoDoc.isEmpty()){
            //funcionesUtiles.clearConsola();
            System.out.print("Ingrese tipo de documento valido: ");
            tipoDoc = scanner.nextLine();
        }
        System.out.print("Ingrese su documento: ");
        String numDoc = scanner.nextLine();
        while(!FuncionesUtiles.contieneSoloNumeros(numDoc) &&  !numDoc.isEmpty()){
            //funcionesUtiles.clearConsola();
            System.out.print("Ingrese numero de documento valido: ");
            numDoc = scanner.nextLine();
        }


        List<HuespedDTO> listaHuespedDTOs = gestorHuesped.buscarDatos(nombreHuesped,apellidoHuesped,tipoDoc,numDoc);

        FuncionesUtiles.clearConsola();
        if (listaHuespedDTOs == null || listaHuespedDTOs.isEmpty()) {
                System.err.println("Dar alta de huésped");
                darAltaHuesped();
        } else {
            // Muestro todos los huéspedes encontrados
            System.out.println("=== RESULTADOS DE LA BÚSQUEDA ===");
            for (int i = 0; i < listaHuespedDTOs.size(); i++) {
                HuespedDTO h = listaHuespedDTOs.get(i);
                System.out.println((i + 1) + ". " + h.getApellido() + ", " + h.getNombre()
                        + " - " + h.getTipoDocumento() + " " + h.getNumeroDocumento());
            }

            // Pregunto si quiere modificar alguno
            Scanner sc = new Scanner(System.in);
            System.out.print("\n¿Desea modificar alguno de los huéspedes encontrados? (S/N): ");
            String respuesta = sc.nextLine().trim();

            if (respuesta.equalsIgnoreCase("S")) {
                System.out.print("Ingrese el número del huésped que desea modificar: ");
                try {
                    int indice = Integer.parseInt(sc.nextLine().trim()) - 1;

                    if (indice >= 0 && indice < listaHuespedDTOs.size()) {
                        HuespedDTO seleccionado = listaHuespedDTOs.get(indice);
                        System.out.println("\nSeleccionado: " + seleccionado.getApellido() + ", " + seleccionado.getNombre());
                        
                        modificarHuesped1(seleccionado,gestorHuesped); 
                    } else {
                        System.out.println("Número inválido. No existe ese huésped en la lista.");
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida. Debe ingresar un número.");
                }
            } else {
                System.out.println("No se modificó ningún huésped.");
            }
        }
        
    }


    /**
     * modificacion del huesped, si el dni existe, se reemplazan los valores en esa posicion
     * @param campos
     * @param huespedDTO
     * @param gestorHuesped
     * @param validadores
     * @param noObligatorios
     * @param huespedDNI
     * @param dniNOMod
     * @param tipoNomod
     */
    public static void modificarHuesped(Map<String, String> campos,HuespedDTO huespedDTO, GestorHuesped gestorHuesped, Map<String, Predicate<String>> validadores,
    Set<String> noObligatorios, HuespedDTO huespedDNI, String dniNOMod, String tipoNomod ) {
        Scanner sc = new Scanner(System.in);
        muestraCamposIngresados(campos);
        // valida campos, pide incorrectos
        System.out.println("PARA ACEPTAR PRESIONE 1: ");
        System.out.println("PARA CANCELAR PRESIONE 2: ");
        System.out.println("PARA BORRAR PRESIONE 3: ");
        int opcion = sc.nextInt();

        if ( opcion == 1) {  //si la opcion es 1 acepta los cambios , entonces valida - SI NO VALIDA EL CMAPO INGRESADO
            System.out.println("DNInomod " + dniNOMod);
            System.out.println("tipoDNInomod: " + tipoNomod);
           System.out.println("Tipo de documento: " + huespedDNI.getTipoDocumento());
           System.out.println("Numero de documento: " + huespedDNI.getNumeroDocumento());
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

    /**
     * se cargan los campos y los validadores y se llama a las funciones correspondientes para modificar el archivo
     * @param huespedDTO
     * @param gestorHuesped
     */
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

    /*    System.out.println("DESEA MODIFICAR OTRO HUESPED? - indique SI o NO ");
    System.out.println("PRESIONE 1.-PARA MODIFICAR OTRO HUESPED");
    System.out.println("PRESIONE 2.-PARA VOLVER AL MENU PRINCIPAL");
    Scanner sc2 = new Scanner(System.in);
    while(!(sc2.equals("SI") && sc2.equals("NO"))){
        System.out.println("Indique SI o NO ");
        sc2.nextLine();

    }

    if( sc.nextLine().equals("si")){
        System.out.println("\n ");
       buscarHuesped();
    }
    else{
        Menu();
    }
*/



    }


    /**
     * valida los campos ingresados
     * @param campos es un map que tiene campo valor
     * @param huespedDNI
     * @param noObligatorios son los no obligatios, pueden quedar vacios
     * @param huespedDTO
     * @param tipoNomod tipodni q esta en el archivo, sin modificar para poder buscar la pos
     * @param dniNomod dni q esta en el archivo sin modificar
     */

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
                    if (campo.equals("numeroDocumento")){
                        huespedDNI.setNumeroDocumento(huespedDTO.getNumeroDocumento());
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
                    if ( campo.equals("tipoDocumento" )){
                        huespedDNI.setTipoDocumento(input.toUpperCase());

                    }
                    else if (campo.equals("numeroDocumento") ){
                        huespedDNI.setNumeroDocumento(input);
                    }
                    ingresado = true;
                }
            }
        }

    }

    /**
     * se muestran campo por campo y valores
     * @param campos
     */
    public static void muestraCamposIngresados(Map<String, String> campos){
        for (Map.Entry<String, String> entry : campos.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    /**
     * es la opcion de aceptar, es decir q se hagan los cambios al modificar el huesped
     * @param campos campos con key-valor
     * @param validadores campo con key-validador
     * @param noObligatorios
     * @param huespedDNI huesped dni guarda el nuevodni y tipo
     * @param dniNOMod
     * @param tipoNomod
     * @param huespedDTO
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
                            if ( campo.equals("tipoDocumento" ) ){
                                huespedDNI.setTipoDocumento(input);

                            }
                            else if (campo.equals("numeroDocumento") ){
                                huespedDNI.setNumeroDocumento(input);
                            }
                            ingresado = true;
                        }
                    }
                    todosValidos = false;
                }
            }
        } while (!todosValidos);


        String tipoDoc = campos.get("tipoDocumento");
        String numeroDoc = campos.get("numeroDocumento");
        //entra a modificar otro huesped en el caso q hayan ingresadp un dni nuevo, en otro caso entra al else
        if ((!(tipoDoc.equalsIgnoreCase(tipoNomod) && (numeroDoc.equalsIgnoreCase(dniNOMod)))) ) {//si es distinto de null es porq ingreso otro dni

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
                    String tipoDocC = sc.nextLine().trim();
                    System.out.println("Ingrese el dni");

                    String valorDoc = sc.nextLine().trim();

                    while (!(Validador.esNumeroValido.test(valorDoc)) && !(Validador.esStringValido.test(tipoDocC))) {  //PUEDE FALLAR
                        System.out.println("INGRESE VALORES VALIDOS");
                        //tenog q validar d enuevo
                        System.out.println("Ingrese el tipo");
                        tipoDocC = sc.nextLine().trim();

                        System.out.println("Ingrese el dni");
                        valorDoc = sc.nextLine().trim();
                    }
                    campos.put("tipoDocumento", tipoDocC);
                    campos.put("numeroDocumento", valorDoc);
                    //muestraCamposIngresados(campos);
                    modificarHuesped(campos, huespedDTO,gestorHuesped,validadores,noObligatorios,huespedDNI,dniNOMod,tipoNomod);

                }
                else {
                    //opta por aceptar igualmente
                    System.out.println("la operacion a culminado con exito ");

                    //ahora llamo a geestor para q llame al dao y guarde en el archivo, le paso el dni ue esta en el archivo para poder buscar


                    System.out.println(huespedDNI.getTipoDocumento());
                    System.out.println(huespedDNI.getNumeroDocumento());


                    gestorHuesped.modificarDatosHuespedArchivo(campos,huespedDTO, "infoBuscarHuespedes.csv", huespedDTO.getDireccionHuesped(), huespedDNI.getTipoDocumento(), huespedDNI.getNumeroDocumento());
                }

            }else{ //opcion de q le quiere modificar el doc al huesped q eligio y ese doc no esta en el archivo
                System.out.println("\n Todos los campos válidos:"); //key - value
                campos.forEach((k, v) -> System.out.println(k + ": " + v));

                System.out.println("la operacion a culminado con exito ");

                //le paso eldni y tipo viejo para q busque pero modifica
                gestorHuesped.modificarDatosHuespedArchivo(campos, huespedDTO, "infoBuscarHuespedes.csv",huespedDTO.getDireccionHuesped(), tipoNomod, dniNOMod );
            }
        } //si huespedDni es null es porq quiere modificar datos de el huesped q esta
        else{  //si no existe el dni, es porq le quiere cambiar a ese q ya esta el dni ENTONCES OCUPA EL DNI VIEJO

            System.out.println("\n Todos los campos válidos:"); //key - value
            campos.forEach((k, v) -> System.out.println(k + ": " + v));

            System.out.println("la operacion a culminado con exito ");

            //ahora llamo a geestor para q llame al dao y guarde en el archivo, le paso el dni ue esta en el archivo para poder buscar
            gestorHuesped.modificarDatosHuespedArchivo(campos, huespedDTO, "infoBuscarHuespedes.csv",huespedDTO.getDireccionHuesped(), tipoNomod, dniNOMod );

            //huespedDAO.actualizarHuesped(rutaArchivo, huespedDTO, huespedDTO.getDireccionHuesped(), tipoDoc, dninoMod);
        }


    }

    /**
     * cuando se cancela la modificacion del huesped, dsp se ve si cancela o vuelve al paso anterior
     * @param campos
     * @param huespedDTO
     * @param gestorHuesped
     * @param validadores
     * @param noObligatorios
     * @param huespedDNI
     * @param dniNOMod
     * @param tipoNomod
     */

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
            //Menu();
        }
        else{ //vuelve a mostrar hasta el boton aceptar borrar cancelar
            modificarHuesped(campos, huespedDTO,gestorHuesped,validadores,noObligatorios,huespedDNI,dniNOMod,tipoNomod);

        }

    }

    /**
 * Da de baja a un huesped del sistema.
 *
 * Recibe un objeto HuespedDTO con los datos del huesped a eliminar.
 * Si el huesped no tiene registros de estadias, se muestra un mensaje
 * de confirmacion para proceder con la baja. En caso de que el huesped
 * haya estado alojado alguna vez, no se permite su eliminacion.
 *
 * El usuario puede confirmar la baja, cancelarla o continuar segun la opcion elegida.
 *
 * @param huespedDTO objeto con los datos del huesped a dar de baja
 */

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


    /**
     * Permite realizar una reserva de habitacion.
     *
     * Solicita al usuario las fechas de ingreso y egreso, validando que el formato sea correcto y que la fecha de inicio sea anterior a la fecha de salida. Luego pide elegir el tipo de habitacion entre las opciones disponibles y muestra su estado segun las fechas elegidas.
     *
     * Si las fechas no son validas, da la posibilidad de volver a ingresarlas
     * o regresar al menu principal.
     */


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
            //funcionesUtiles.clearConsola();
            //System.out.println("Desde Fecha: "+desdeFecha);
            //System.out.println("Hasta Fecha: "+hastaFecha + "\n");

            Integer opcionHabitacion;

            System.out.println("Escoja el tipo de habitación que desea: ");
            System.out.println("1. Individual Estandar");
            System.out.println("2. Doble Estandar");
            System.out.println("3. Doble Superior");
            System.out.println("4. Superior Family Plan");
            System.out.println("5. Suite Doble");

            opcionHabitacion = scanner.nextInt();

            while (opcionHabitacion<1 && opcionHabitacion>5){
                System.out.println("ERROR. Por favor ingrese una opcion valida.");
                System.out.println("1. Individual Estandar");
                System.out.println("2. Doble Estandar");
                System.out.println("3. Doble Superior");
                System.out.println("4. Superior Family Plan");
                System.out.println("5. Suite Doble");
            }
            
            String nombreTipoHabitacion="";
            switch (opcionHabitacion) {
                case 1:
                    nombreTipoHabitacion="Individual Estandar";
                    break;
                case 2:
                    nombreTipoHabitacion="Doble Estandar";
                    break;
                case 3:
                    nombreTipoHabitacion="Doble Superior";
                    break;
                case 4:
                    nombreTipoHabitacion="Superior Family Plan";
                    break;
                case 5:
                    nombreTipoHabitacion="Suite Doble";
                    break;
            }

            FuncionesUtiles.clearConsola();
            System.out.println("FECHAS SELECCIONADAS: "+FuncionesUtiles.convertirDateAString(desdeFecha)+" hasta el "+FuncionesUtiles.convertirDateAString(hastaFecha));
            System.out.println("HABITACION SELECCIONADA: "+nombreTipoHabitacion);

            mostrarEstadoHabitaciones(nombreTipoHabitacion,desdeFecha,hastaFecha); //CU05
        } else{
            FuncionesUtiles.clearConsola();
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
                FuncionesUtiles.clearConsola();
                reservarHabitacion();
            } else{
                FuncionesUtiles.clearConsola();
                Menu();
            }
        }
    }

    /**
     * Muestra el estado de las habitaciones segun el tipo y el rango de fechas indicado.
     *
     * Llama al gestor de habitaciones para mostrar la disponibilidad
     * y luego permite al usuario elegir entre realizar una reserva
     * o volver a ingresar los datos de busqueda.
     *
     * @param tipoHabitacion tipo de habitacion a consultar
     * @param desdeFecha fecha inicial del rango
     * @param hastaFecha fecha final del rango
     */

    public static void mostrarEstadoHabitaciones(String tipoHabitacion, Date desdeFecha, Date hastaFecha){
        Scanner scanner = new Scanner(System.in);

        //funcionesUtiles.clearConsola();
        System.out.println("MOSTRAR ESTADO DE HABITACIONES");        
        gestorHabitacion.muestraEstado(tipoHabitacion,desdeFecha,hastaFecha);

        Integer opcion;

        System.out.println("Ingrese una opcion: ");
        System.out.println("1. Reservar");
        System.out.println("2. Volver a Ingresar Datos de Busqueda");
        
        opcion= scanner.nextInt();

        while (opcion!=1 && opcion!=2){
            System.out.println("ERROR. Por favor ingrese una opcion valida.");
            System.out.println("1. Reservar");
            System.out.println("2. Volver a Ingresar Datos de Busqueda");
        } FuncionesUtiles.clearConsola();
        if(opcion==1){

        } else{
            reservarHabitacion();
        }
    }
    /**
     * Valida una fecha ingresada y la convierte en un objeto Date.
     * Si la fecha no tiene el formato correcto, devuelve null.
     *
     * @param fechaIngresada texto con la fecha a validar
     * @param formatter formato esperado para la fecha
     * @return objeto Date si la conversion fue exitosa, null en caso contrario
     */

    public static Date validarFecha(String fechaIngresada, SimpleDateFormat formatter){
        try {
            return formatter.parse(fechaIngresada);
        } catch (ParseException e) {
            return null; // devuelve null si no se pudo parsear
        }
    }
    /**
     * Verifica que la primera fecha sea anterior a la segunda.
     *
     * @param desdeFecha fecha de inicio
     * @param hastaFecha fecha de fin
     * @return true si la primera fecha es anterior a la segunda, false en caso contrario
     */

    public static boolean validarAmbasFechas(Date desdeFecha, Date hastaFecha){
        if(desdeFecha.before(hastaFecha)){
            return true;    
        } else {
            return false;
        }
    }



    //metodos cancelar reserva cu06

    public static void ingresoDatosCancelacion (String apellido, String nombre){

        System.out.println("Proceso cancelacion de reserva \n");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese apellido");
        apellido = scanner.nextLine();

        System.out.println("Ingrese nombre");
        nombre = scanner.nextLine();

        while (apellido.equalsIgnoreCase("")) {
            System.out.println("Apellido no puede ser vacio");
            apellido = scanner.nextLine();
        }
    }


    public static void cancelarReserva(GestorReservaInterfaz gestorReserva){

        String apellido="", nombre="";

        ingresoDatosCancelacion(apellido,nombre);

/*        if (gestorReserva.cancelarReserva() ){
            "No existen reservas para los criterios de busqueda"

        }

*/

    }


}

