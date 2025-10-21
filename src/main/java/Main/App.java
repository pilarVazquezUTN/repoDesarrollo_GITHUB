package Main;
/*
 IMPORTAR TODAS LAS CLASES DE UN PACKAGE: import <paquetess>.*; ejmplo: import Classes.Usuario.*;
 IMPORTAR CLASES QUE YO QUIERA DEL PACKAGE: import <paquetess>.<clase>; ejemplo: como esta en el tp
*/
import Classes.Usuario.UsuarioDTO;
import Classes.Usuario.UsuarioDAO;
import Classes.Direccion.Direccion;
import Classes.Direccion.DireccionDTO;
import Classes.Huesped.HuespedDAO;
import Classes.Huesped.HuespedDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        
        Menu();
    }

    public static void Menu(){
        //System.out.println("funciona");
        System.out.println("\n Opciones: ");
        System.out.println("1. Autenticar Usuario"); 
        System.out.println("2. Buscar Huesped"); 
        System.out.println("3. Dar De Alta Huesped"); 
        System.out.println("4. Modfiicar Huesped"); 
        System.out.println("5. Dar de Baja Huesped"); 
        System.out.print("--- Ingrese una opción: "); 

        ingresaOpcion();
    }

    public static void ingresaOpcion(){
        Scanner scanner = new Scanner(System.in);
        
        int opcion= scanner.nextInt();
        
        while(opcion<1 || opcion>5){
            clearConsola();
            System.out.println("--------------- Ingrese una opcion correcta!");
            Menu();
            break;
        }

        switch (opcion) {
            case 1://autenticar usuario
                autenticarHuesped();
                break;
            case 2://buscar huesped
                buscarHuesped();
                break;
            case 3://daar de alta huesped
                darAltaHuesped();                
                break;
            case 4:// modificar huesped
                
                break;                
            case 5://dar de baja huesped
                
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

        //llamamos al DAO
        UsuarioDAO usuarioDAO= new UsuarioDAO();
        
        clearConsola();

        if(usuarioDAO.buscarUsuario(usuarioDTO)){
            System.out.println("encontrado \n");
        } else {
            System.out.println("noooooooo encontrado \n");
        }

        Menu();

        
    }

    public static void darAltaHuesped(){
        Scanner scanner = new Scanner(System.in);
        HuespedDTO huespedDTO = new HuespedDTO();
        HuespedDAO huespedDAO = new HuespedDAO();
        
        huespedDTO=ingresarDatos();
        huespedDAO.registrarHuesped(huespedDTO);
        
        System.out.println("El huésped " + huespedDTO.getNombre() +","+huespedDTO.getApellido() + 
        " ha sido atisfactoriamente cargado al sistema. \n ¿Desea cargar otro? \n" +
        "1. si \n 2. no");
        
        Integer opcion= scanner.nextInt();
        if(opcion==1){
            //vuelve a que cargue mas huespedes
            darAltaHuesped();
        } 
    }
    public static HuespedDTO ingresarDatos(){
        HuespedDTO huespedDTO = new HuespedDTO();
        Scanner scanner = new Scanner(System.in);
        Date fechaNacDTO;
        DireccionDTO direccionDTO=new DireccionDTO();

        System.out.print("Ingrese su nombre: ");
        huespedDTO.setNombre(scanner.nextLine());

        System.out.print("Ingrese su apellido: ");
        huespedDTO.setApellido(scanner.nextLine());

        System.out.print("Ingrese Tipo de documento: ");
        huespedDTO.setTipoDocumento(scanner.nextLine());

        System.out.print("Ingrese su numero de documento: ");
        huespedDTO.setNumeroDocumento(scanner.nextLine());

        System.out.print("Ingrese su CUIT: ");
        huespedDTO.setCuit(scanner.nextLine());

        System.out.print("Ingrese su posicion frente al IVA: ");
        huespedDTO.setPosicionIva(scanner.nextLine());

        System.out.print("Ingrese su fecha de nacimiento (dd/MM/yyyy): ");
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

        System.out.print("Ingrese su calle: ");
        direccionDTO.setCalle(scanner.nextLine());

        System.out.print("Ingrese su número de calle: ");
        direccionDTO.setNumero(scanner.nextLine());

        System.out.print("Ingrese su departamento: ");
        direccionDTO.setDepartamento(scanner.nextLine());

        System.out.print("Ingrese su piso: ");
        direccionDTO.setPiso(scanner.nextLine());

        System.out.print("Ingrese su código postal: ");
        direccionDTO.setCodigoPostal(scanner.nextLine());

        System.out.print("Ingrese su localidad: ");
        direccionDTO.setLocalidad(scanner.nextLine());

        System.out.print("Ingrese su provincia: ");
        direccionDTO.setProvincia(scanner.nextLine());

        System.out.print("Ingrese su pais: ");
        direccionDTO.setPais(scanner.nextLine());

        huespedDTO.setDireccionHuesped(direccionDTO);

        System.out.print("Ingrese su telefono: ");
        huespedDTO.setTelefono(scanner.nextLine());

        System.out.print("Ingrese su email: ");
        huespedDTO.setEmail(scanner.nextLine());

        System.out.print("Ingrese su ocupación: ");
        huespedDTO.setOcupacion(scanner.nextLine());

        System.out.print("Ingrese su nacionalidad: ");
        huespedDTO.setNacionalidad(scanner.nextLine());

        System.out.println("1. Siguiente");
        System.out.println("2. Cancelar");

        Integer opcion =scanner.nextInt();

        return huespedDTO; //FALTA VER QUE PASA SI PRESIONA CANCELAR   
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
        System.out.print("Ingrese su nombre: ");
        String nombreHuesped = scanner.nextLine();
        System.out.print("Ingrese su apellido: ");
        String apellidoHuesped = scanner.nextLine();
        System.out.print("Ingrese Tipo de documento: ");
        String tipoDoc = scanner.nextLine();
        System.out.print("Ingrese su documento: ");
        String numDoc = scanner.nextLine();
        HuespedDAO huespedDAO= new HuespedDAO();
        HuespedDTO huespedDTO= new HuespedDTO();
        huespedDTO=huespedDAO.buscarDatos(nombreHuesped,apellidoHuesped,tipoDoc,numDoc);
        System.out.println("Huesped seleccionado: ");
        System.out.println("  Nombre: " + huespedDTO.getNombre());  
        System.out.println("  Apellido: " + huespedDTO.getApellido());
        System.out.println("  Tipo documento: " + huespedDTO.getTipoDocumento());
        System.out.println("  N° documento: " + huespedDTO.getNumeroDocumento());
        modificarHuesped(huespedDTO);
    }
   
}