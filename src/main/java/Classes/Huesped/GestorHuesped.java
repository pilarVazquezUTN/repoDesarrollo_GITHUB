package Classes.Huesped;

import Classes.Direccion.DireccionDTO;
import Classes.Validador;
import Classes.DAOFactory;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;

public class GestorHuesped {
    private String idEmpleado;
    HuespedDAO huespedDAO = (HuespedDAO) DAOFactory.create(DAOFactory.HUESPED);

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }
    public String getIdEmpleado() {
        return this.idEmpleado;
    }

    public void  buscarHuesped(){

    } 
    public Huesped darAltaHuesped(){
        return null;
    }
    public void darBajaHuesped(){

    }

    public boolean chequearExisteHuesped(HuespedDTO huespedDTO){
        return huespedDAO.verificarDocumento(huespedDTO);
    }

    /**
     *
     * @param huespedDTO datos
     * @param huespedDAO instancia del Dao
     * @param rutaArchivo ruta donde esta el archivo
     *
     *
     *el gestor le pasa al dao los datos y la ruta donde tiene q buscar
     */
    public void modificarHuespedGestor(HuespedDTO huespedDTO, DireccionDTO d, String rutaArchivo, Map<String, String> campos, Map<String,Predicate<String>> validadores ) {

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");


        String nuevo; //nuevo es la variable para ir guardando los valores a modificar




        //esto entonces son los datos de el huesped
        campos.put("apellido", huespedDTO.getApellido());
        campos.put("nombre", huespedDTO.getNombre());
        campos.put("tipoDocumento", huespedDTO.getTipoDocumento());
        campos.put("numeroDocumento", huespedDTO.getNumeroDocumento());
        campos.put("CUIT", huespedDTO.getCuit());
        campos.put("posicionIva", huespedDTO.getPosicionIva());
        campos.put("fechaNacimiento", formato.format(huespedDTO.getFechaNacimiento())); //hacer el format

         d = huespedDTO.getDireccionHuesped();
        campos.put("calle", d.getCalle());
        campos.put("numero", d.getNumero());
        campos.put("departamento", d.getDepartamento());
        campos.put("piso", d.getPiso());
        campos.put("codigoPostal", d.getCodigoPostal());
        campos.put("localidad", d.getLocalidad());
        campos.put("provincia", d.getProvincia());
        campos.put("pais", d.getPais());
        campos.put("telefono", huespedDTO.getTelefono());
        campos.put("email", huespedDTO.getEmail());
        campos.put("ocupacion", huespedDTO.getOcupacion());
        campos.put("nacionalidad", huespedDTO.getNacionalidad());



        // aca pongo q predicado valida cada campo

        validadores.put("nombre", Validador.esStringValido);
        validadores.put("apellido", Validador.esStringValido);
        validadores.put("tipoDocumento", Validador.esStringValido);
        validadores.put("numeroDocumento", Validador.esNumeroValido);


        validadores.put("CUIT", Validador.esCuitValido);
        validadores.put("posicionIva", Validador.esStringValido);
        validadores.put("fechaNacimiento", Validador.esFechaValida);
        validadores.put("calle", Validador.esCalleValida); //
        validadores.put("numero", Validador.esNumeroValido);
        validadores.put("departamento", Validador.esStringValido);
        validadores.put("piso", Validador.esNumeroValido);
        validadores.put("codigoPostal", Validador.esNumeroValido);
        validadores.put("localidad", Validador.esStringValido);
        validadores.put("provincia",Validador.esStringValido);
        validadores.put("pais", Validador.esStringValido);
        validadores.put("telefono", Validador.esNumeroValido);
        validadores.put("email",Validador.esMailValido);
        validadores.put("ocupacion", Validador.esStringValido);
        validadores.put("nacionalidad", Validador.esStringValido);


        //defino ciales son los obligatorios, hago set mas facil para buscar campos


    }

    /**
     *
     * @param huespedDTO
     * @param rutaArchivo
     * @param dni
     * @param tipo
     * le pasa al dao los datos, TIENE Q BUSCAR Y REEMPLAZAR
     */
    public void modificarDatosHuespedArchivo(HuespedDTO huespedDTO, String rutaArchivo, DireccionDTO direccionDTO, String tipo , String dni) {
        boolean huespedModificado = false;
        huespedModificado= huespedDAO.actualizarHuesped(rutaArchivo,huespedDTO,direccionDTO,tipo,dni);


    }







    public void registrarHuesped(HuespedDTO huespedDTO) {
        /*
         llamar a la funcion del dao para que guarde los datos.
         */
        huespedDAO.registrarHuesped(huespedDTO);
    }
    public boolean verificarDocumento(HuespedDTO huespedDTO){
        /*
         chequear que el tipo y numero de documento no existan
         */
        return huespedDAO.verificarDocumento(huespedDTO);
    }

    public boolean eliminarHuesped(HuespedDTO huespedDTO) {

        return huespedDAO.eliminarHuespued(huespedDTO);
    }

    public boolean seAlojo (HuespedDTO huespedDTO) {
        return huespedDAO.seAlojo(huespedDTO);
    }

    public HuespedDTO buscarDatos(String nombreHuesped, String apellidoHuesped, String tipoDoc,String numDoc){
        return huespedDAO.buscarDatos(nombreHuesped,apellidoHuesped,tipoDoc,numDoc);
    }
}
