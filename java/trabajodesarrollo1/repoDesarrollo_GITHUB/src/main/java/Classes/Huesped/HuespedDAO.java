package Classes.Huesped;

import Classes.Direccion.DireccionDTO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class HuespedDAO {
    public void delete(){ 

    }
    public  void create(){
    }
    public  void update(){
    }
    public  void read(){
    }

    public static void registrarHuesped(HuespedDTO huespedDTO){
        /*
         AGREGAR EL HUESPEDDTO QUE LLEGA A LA BD DE HUESPEDES
         */
    }


    /**
     *
     * @param nombreHuesped
     * @param apellidoHuesped
     * @param tipoDoc
     * @param numDoc
     * @return
     * DIRECCIONdto TIENE 8 ATRIBUTOS, ESTAN EN EL DOC, formato: calle num depar piso cp loc prov pais
     */
    public static HuespedDTO buscarDatos(String nombreHuesped, String apellidoHuesped, String tipoDoc,String numDoc){
        String rutaArchivo = "infoBuscarHuespedes.txt"; // esta dentro de la carpeta todos los archivos txt no hace falta ruta
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

