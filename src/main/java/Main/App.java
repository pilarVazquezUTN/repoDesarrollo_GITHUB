package Main;
/*
 IMPORTAR TODAS LAS CLASES DE UN PACKAGE: import <paquetess>.*; ejmplo: import Classes.Usuario.*;
 IMPORTAR CLASES QUE YO QUIERA DEL PACKAGE: import <paquetess>.<clase>; ejemplo: como esta en el tp
*/
import Classes.Usuario.UsuarioDTO;
import Classes.Usuario.GestorUsuario;
import Classes.Usuario.UsuarioDAO;
import Classes.Direccion.DireccionDTO;
import Classes.Huesped.GestorHuesped;
import Classes.Huesped.HuespedDAO;
import Classes.Huesped.HuespedDTO;

import java.io.IOException;
import java.text.ParseException; 
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Scanner;



public class App {
    static GestorHuesped gestorHuesped=new GestorHuesped();
    static GestorUsuario gestorUsuario= new GestorUsuario();
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
        clearConsola();
        autenticarHuesped();

    }
    public static void Menu(){
        System.out.println("--------------- MENU ----------------");
        System.out.println("\n Opciones: ");
        System.out.println("1. Buscar Huesped"); 
        System.out.println("2. Dar De Alta Huesped");
        System.out.println("3. Modfiicar Huesped");  //para este se tiene que haber ejecutado primero el buscar huesped
        System.out.println("4. Dar de Baja Huesped"); 
        System.out.print("--- Ingrese una opción: "); 

        ingresaOpcion();
    }

    public static void ingresaOpcion(){
        Scanner scanner = new Scanner(System.in);
        boolean entradaValida=false;
        int opcion= scanner.nextInt();
        
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
            case 2://daar de alta huesped
                System.out.println("DAR DE ALTA HUESPED \n");
                darAltaHuesped();                
                break;
            case 3:// modificar huesped
                System.out.println("MODIFICAR HUESPED \n");
                modificarHuesped(null, gestorHuesped);
                break;
            case 4://dar de baja huesped
                System.out.println("DAR DE BAJA HUESPED \n");
                break;                
        }
    }

    public static void autenticarHuesped(){
        /*se presenta pantalla para autenticar usuario  
         ingresa nombre, contraseña(HACER LOS *****)
         mostrar error de algun dato invalido
         sistema blanquea campos cuando esten OK
         se vuelve al Menu
        */
        Scanner scanner = new Scanner(System.in);
        String nombreUsuario;
        String contrasenaUsuario;

        System.out.print("Ingrese su nombre: ");
        nombreUsuario= scanner.next();
        System.out.print("Ingrese su contraseña: ");
        contrasenaUsuario= scanner.next();

        //creamos el usuarioDTO para pasarselo al usuarioDAO
        UsuarioDTO usuarioDTO = new UsuarioDTO();

        usuarioDTO.setNombre(nombreUsuario);
        usuarioDTO.setContrasena(contrasenaUsuario);

        UsuarioDAO usuarioDAO= new UsuarioDAO();
        clearConsola();
        //llamamos al Gestor
        if(gestorUsuario.autenticarUsuario(usuarioDAO,usuarioDTO)){
            System.out.println("Usuario Encontrado. Acceso concedido. \n");
            Menu();
        } else{
            System.out.println("usuario no encontrado. Se vuelve a Autenticacion de Usuario. \n");
            autenticarHuesped();
        }


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
            " ha sido atisfactoriamente cargado al sistema. \n ¿Desea cargar otro? \n" +
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
                    //System.err.println("❌ ERROR: El formato de fecha ingresado es INCORRECTO. La fecha no fue guardada.");
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
                //System.err.println("❌ ERROR: El formato de fecha ingresado es INCORRECTO. La fecha no fue guardada.");
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

        GestorHuesped gestorHuesped = new GestorHuesped();

        System.out.print("Ingrese su nombre: ");
        String nombreHuesped = scanner.nextLine();
        System.out.print("Ingrese su apellido: ");
        String apellidoHuesped = scanner.nextLine();
        System.out.print("Ingrese Tipo de documento: ");
        String tipoDoc = scanner.nextLine();
        System.out.print("Ingrese su documento: ");
        String numDoc = scanner.nextLine();
        HuespedDAO huespedDAO= new HuespedDAO();
        HuespedDTO huespedDTO = new HuespedDTO();
        huespedDTO=huespedDAO.buscarDatos(nombreHuesped,apellidoHuesped,tipoDoc,numDoc);
        System.out.println("Huesped seleccionado: ");
        System.out.println("  Nombre: " + huespedDTO.getNombre());  
        System.out.println("  Apellido: " + huespedDTO.getApellido());
        System.out.println("  Tipo documento: " + huespedDTO.getTipoDocumento());
        System.out.println("  N° documento: " + huespedDTO.getNumeroDocumento());
        modificarHuesped(huespedDTO,gestorHuesped); //aca llamo a modificar huesped con Huesped DTO Y HuespedDto debe tener todos los campos
        //nose si deberia llamar al gestor o a la clase
    }

    /**
     *
     * @param huespedDTO
     * @param gestorHuesped
     * yo aca agarro mis daots del huespedDTO, los tengo q mostrar por pantalla dsp mando al gestor para q llame al dao
     * FALTA VALIDAR LOS DATOS DE CADA CAMPO QUE MODIFICA, (hice de campos string) SI PRESIONA BORRAR ---- DEBE VOLVER A PEDIR Q INGRESE PORQ ES OBLIGATORIO
     */




    public static void modificarHuesped(HuespedDTO huespedDTO, GestorHuesped gestorHuesped) {
        String dniNOMod = huespedDTO.getNumeroDocumento();
        Scanner sc = new Scanner(System.in);


        String nuevo; //en cada campo obligatorio seria un loop para q vuevla a ingresar ese cmapo



        System.out.println("Nombre: " + huespedDTO.getNombre());
        System.out.print("modificar nombre (enter para mantener) - 'b' para borrar");
        nuevo = sc.nextLine().trim();

        if (!nuevo.isEmpty()) {
            if (nuevo.equals("b")) {
                System.out.println("no se puede borrar campo obligatorio");
            }
            else {
                if (esStringValido.esValido(nuevo))  {
                    huespedDTO.setNombre(nuevo);
                }
                else{
                    while (!esStringValido.esValido(nuevo)) {
                      System.out.println("Ingrese un nombre valido");
                        nuevo = sc.nextLine().trim();
                    }
                    //se supone q para salir del while tuvo q haber puesto bien pero lo pongo por las du
                    if (esStringValido.esValido(nuevo)) {
                        huespedDTO.setNombre(nuevo);
                    }
                }

            }
        }

        System.out.println("Apellido: " + huespedDTO.getApellido());
        System.out.print("modificar apellido (enter para mantener): - 'b' para borrar ");
         nuevo = sc.nextLine().trim();
        if (!nuevo.isEmpty()) {
            if (nuevo.equals("b")) {
                System.out.println("no se puede borrar campo obligatorio");
            }
            else {
                if (esStringValido.esValido(nuevo))  {
                    huespedDTO.setApellido(nuevo);
                }
                else{
                    while (!esStringValido.esValido(nuevo)) {
                        System.out.println("Ingrese un nombre valido");
                        nuevo = sc.nextLine().trim();
                    }
                    //se supone q para salir del while tuvo q haber puesto bien pero lo pongo por las du
                    if (esStringValido.esValido(nuevo)) {
                        huespedDTO.setApellido(nuevo);
                    }
                }

            }
        }


        System.out.println("Tipo documento: " + huespedDTO.getTipoDocumento());
        System.out.print("modificar tipo documento (enter para mantener): - 'b' para borrar ");
         nuevo = sc.nextLine().trim();
        if (!nuevo.isEmpty()) {
            if (nuevo.equals("b")) {
                System.out.println("no se puede borrar campo obligatorio");
            }
            else {
                huespedDTO.setTipoDocumento(nuevo);
            }
        }
        System.out.println("N° documento: " + huespedDTO.getNumeroDocumento());
        System.out.print("modificar número documento (enter para mantener): - 'b' para borrar ");
         nuevo = sc.nextLine().trim();
        if (!nuevo.isEmpty()) {
            if (nuevo.equals("b")) {
                System.out.println("no se puede borrar campo obligatorio");
            }
            else {
                huespedDTO.setNumeroDocumento(nuevo);
            }
        }
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("Fecha de nacimiento: " + formato.format(huespedDTO.getFechaNacimiento()));
        System.out.print("Modificar fecha (dd/MM/yyyy) (Enter para mantener): - 'b' para borrar: ");

         nuevo = sc.nextLine().trim();

        if (!nuevo.isEmpty()) {
            if (nuevo.equalsIgnoreCase("b")) {
                System.out.println("No se puede borrar campo obligatorio.");
            } else {
                try {
                    Date nuevaFecha = formato.parse(nuevo); // convierte el String a Date
                    huespedDTO.setFechaNacimiento(nuevaFecha);
                    System.out.println("Fecha modificada correctamente: " + formato.format(nuevaFecha));
                } catch (ParseException e) {
                    System.out.println("Formato de fecha inválido. Debe ser dd/MM/yyyy.");
                }
            }
        } else {
            System.out.println("No se modificó la fecha.");
        }


        System.out.println("Teléfono: " + huespedDTO.getTelefono());
        System.out.print("modificar teléfono (enter para mantener) - 'b' para borrar ");

        nuevo= sc.nextLine().trim();
        if (!nuevo.isEmpty()) {
            if (nuevo.equals("b")) {
                System.out.println("no se puede borrar campo obligatorio");
            }
            else {
                huespedDTO.setTelefono(nuevo);
            }
        }

        System.out.println("Email: " + huespedDTO.getEmail());
        System.out.print("modificar email (enter para mantener): - 'b' para borrar ");
         nuevo = sc.nextLine().trim();
        if (!nuevo.isEmpty()) {
            if (nuevo.equals("b")) {
                huespedDTO.setEmail("");
            }
            else {
                huespedDTO.setEmail(nuevo);
            }
        }
//creo dto de direccion para ir guardando
        System.out.println("\nDIRECCIÓN");
        DireccionDTO d = huespedDTO.getDireccionHuesped();


        System.out.print("modificar direccion (enter para mantener): - 'b' para borrar ");
        System.out.print("Calle (" + d.getCalle() + "): ");
         nuevo = sc.nextLine().trim();
        if (!nuevo.isEmpty()) {
            if (nuevo.equals("b")) {
                System.out.println("no se puede borrar campo obligatorio");
            }
            else {
               d.setCalle(nuevo);
            }
        }

        System.out.print("Número (" + d.getNumero() + "): ");
         nuevo = sc.nextLine().trim();
        if (!nuevo.isEmpty()) {
            if (nuevo.equals("b")) {
                System.out.println("no se puede borrar campo obligatorio");
            }
            else {
                d.setNumero(nuevo);
            }
        }

        System.out.print("Localidad (" + d.getLocalidad() + "): ");
        nuevo = sc.nextLine().trim();
        if (!nuevo.isEmpty()) {
            if (nuevo.equals("b")) {
                System.out.println("no se puede borrar campo obligatorio");
            }
            else {
                d.setLocalidad(nuevo);
            }
        }

        System.out.print("Departamento (" + d.getDepartamento() + "): ");
         nuevo= sc.nextLine().trim();
        if (!nuevo.isEmpty()) {
            if (nuevo.equals("b")) {
                System.out.println("no se puede borrar campo obligatorio");
            }
            else {
                d.setDepartamento(nuevo);
            }
        }

        System.out.print("Piso (" + d.getPiso() + "): ");
         nuevo = sc.nextLine().trim();
        if (!nuevo.isEmpty()) {
            if (nuevo.equals("b")) {
                System.out.println("no se puede borrar campo obligatorio");
            }
            else {
                d.setPiso(nuevo);
            }
        }
        System.out.print("Código Postal (" + d.getCodigoPostal() + "): ");
         nuevo = sc.nextLine().trim();
        if (!nuevo.isEmpty()) {
            if (nuevo.equals("b")) {
                System.out.println("no se puede borrar campo obligatorio");
            }
            else {
                d.setCodigoPostal(nuevo);
            }
        }
        System.out.print("Provincia (" + d.getProvincia() + "): ");
         nuevo = sc.nextLine().trim();
        if (!nuevo.isEmpty()) {
            if (nuevo.equals("b")) {
                System.out.println("no se puede borrar campo obligatorio");
            }
            else {
                if (esStringValido.esValido(nuevo))  {
                    d.setProvincia(nuevo);
                }
                else{
                    while (!esStringValido.esValido(nuevo)) {
                        System.out.println("Ingrese un nombre valido");
                        nuevo = sc.nextLine().trim();
                    }
                    //se supone q para salir del while tuvo q haber puesto bien pero lo pongo por las du
                    if (esStringValido.esValido(nuevo)) {
                        d.setProvincia(nuevo);
                    }
                }





        }}

        System.out.print("País (" + d.getPais() + "): ");
         nuevo= sc.nextLine().trim();
        if (!nuevo.isEmpty()) {
            if (nuevo.equals("b")) {
                System.out.println("no se puede borrar campo obligatorio");
            }
            else {
                d.setPais(nuevo);
            }
        }

        System.out.println("modificar datos (enter para mantener) - 'b' para borrar");
        System.out.print("CUIT (" + huespedDTO.getCuit() + "): ");
         nuevo= sc.nextLine().trim();
        if (!nuevo.isEmpty()) {
            if (nuevo.equals("b")) {
                System.out.println("no se puede borrar campo obligatorio");
            }
            else {
                huespedDTO.setCuit(nuevo);
            }
        }

        System.out.print("Posición IVA (" + huespedDTO.getPosicionIva() + "): ");
         nuevo = sc.nextLine().trim();
        if (!nuevo.isEmpty()) {
            if (nuevo.equals("b")) {
                System.out.println("no se puede borrar campo obligatorio");
            }
            else {
                huespedDTO.setPosicionIva(nuevo);
            }
        }

        System.out.print("Ocupación (" + huespedDTO.getOcupacion() + "): ");
         nuevo = sc.nextLine().trim();
        if (!nuevo.isEmpty()) {
            if (nuevo.equals("b")) {
                System.out.println("no se puede borrar campo obligatorio");
            }
            else {
                huespedDTO.setOcupacion(nuevo);
            }
        }

        System.out.print("Nacionalidad (" + huespedDTO.getNacionalidad() + "): ");
         nuevo = sc.nextLine().trim();
        if (!nuevo.isEmpty()) {
            if (nuevo.equals("b")) {
                System.out.println("no se puede borrar campo obligatorio");
            }
            else {
                huespedDTO.setNacionalidad(nuevo);
            }
        }


        System.out.println("datos finales \n");


        System.out.println("Datos del Huesped"); System.out.println(" Nombre: " + huespedDTO.getNombre());
        System.out.println(" Apellido: " + huespedDTO.getApellido());
        System.out.println(" Tipo documento: " + huespedDTO.getTipoDocumento());
        System.out.println(" N° documento: " + huespedDTO.getNumeroDocumento());
        System.out.println("Fecha de nacimiento: " + formato.format(huespedDTO.getFechaNacimiento()));
        System.out.println("Teléfono: " + huespedDTO.getTelefono());
        System.out.println("Email: " + huespedDTO.getEmail());
        System.out.println("DIRECCIÓN DEL HUESPED");
        DireccionDTO dir = huespedDTO.getDireccionHuesped(); if (dir != null) { System.out.println("Calle: " + d.getCalle());
        System.out.println("Número: " + d.getNumero()); System.out.println("Localidad: " + d.getLocalidad());
        System.out.println("Departamento: " + d.getDepartamento()); System.out.println("Piso: " + d.getPiso());
        System.out.println("Código Postal: " + d.getCodigoPostal()); System.out.println("Provincia: " + d.getProvincia());
        System.out.println("País: " + d.getPais()); } else { System.out.println("No se encontró dirección cargada."); }
        System.out.println("CUIT: " + huespedDTO.getCuit()); System.out.println("Posición IVA: " + huespedDTO.getPosicionIva());
        System.out.println("Ocupación: " + huespedDTO.getOcupacion()); System.out.println("Nacionalidad: " + huespedDTO.getNacionalidad());

        System.out.println("la operacion a culminado con exito ");

        //ahora llamo a geestor para q llame al dao y guarde en el archivo, le paso el dni ue esta en el archivo para poder buscar
        gestorHuesped.modificarHuesped(huespedDTO,"infoBuscarHuespedes.txt",dniNOMod );


    }

    /**
     * interface funcional para chequear q sea palabra sin car y num
     * @param <T>
     */
    @FunctionalInterface
    interface Validador<T> {
        boolean esValido(T valor);
    }

    static Validador<String> esStringValido = valor ->
            valor.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");

}

//public static void darBajaHuesped (){}