package Main;
package Classes;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class App {
    public static void main(String[] args) throws Exception {
        
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
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("--------------- Ingrese una opcion correcta!");
            Menu();
            break;
        }

        switch (opcion) {
            case 1://autenticar usuario
                autenticarHuesped();
                break;
            case 2://buscar huesped
                
                break;
            case 3://daar de alta huesped
                
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
        String contraseñaUsuario;

        System.out.print("Ingrese su nombre: ");
        nombreUsuario= scanner.next();
        System.out.print("Ingrese su contraseña: ");
        contraseñaUsuario= scanner.next();

        System.out.println("nombre: "+ nombreUsuario + ", contraseña: " + contraseñaUsuario);

        
    }



    public class LeerArchivo {

        public static void main(String[] args) {
        // Asegúrate de que la ruta del archivo es correcta
        String rutaArchivo = "D:\UTN\2025\DesarrolloSoftware\trabajoPractico\infoAutenticarHuesped.txt";

        // Se recomienda el uso de try-with-resources para cerrar automáticamente los flujos
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            }
        }
    }

}


