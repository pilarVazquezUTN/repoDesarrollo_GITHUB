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

    /**
     *
     * @param idEmpleado
     */
    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    /**
     *
     * @return
     */
    public String getIdEmpleado() {
        return this.idEmpleado;
    }

    /**
     *buscar huesped
     */
    public void  buscarHuesped(){

    }

    /**
     * dar alta un huesped
     * @return
     */
    public Huesped darAltaHuesped(){
        return null;
    }

    /**dar de baja un huesped
     *
     */
    public void darBajaHuesped(){

    }

    /**
     * verificar si ya existe el huesped
     * @param huespedDTO
     * @return
     */
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
    public void modificarDatosHuespedArchivo(Map<String, String> campos, HuespedDTO huespedDTO, String rutaArchivo, DireccionDTO direccionDTO, String tipo , String dni) {
        boolean huespedModificado = false;
        huespedModificado= huespedDAO.actualizarHuesped( campos, rutaArchivo,huespedDTO,direccionDTO,tipo,dni);


    }


    /**
     * se registra el hueesped
     * @param huespedDTO
     */
    public void registrarHuesped(HuespedDTO huespedDTO) {
        /*
         llamar a la funcion del dao para que guarde los datos.
         */
        huespedDAO.registrarHuesped(huespedDTO);
    }

    /**
     * sse verifica el documento del huesped
     * @param huespedDTO
     * @return
     */
    public boolean verificarDocumento(HuespedDTO huespedDTO){
        /*
         chequear que el tipo y numero de documento no existan
         */
        return huespedDAO.verificarDocumento(huespedDTO);
    }

    /**
     * se pasa al dao los datos para eliminar un huesped
     * @param huespedDTO
     * @return
     */
    public boolean eliminarHuesped(HuespedDTO huespedDTO) {

        return huespedDAO.eliminarHuespued(huespedDTO);
    }

    /**
     * si se alojo en el hotel no se puede eliminar
     * @param huespedDTO
     * @return
     */
    public boolean seAlojo (HuespedDTO huespedDTO) {
        return huespedDAO.seAlojo(huespedDTO);
    }

    /**
     * busca los datos del huesped
     * @param nombreHuesped
     * @param apellidoHuesped
     * @param tipoDoc
     * @param numDoc
     * @return
     */
    public HuespedDTO buscarDatos(String nombreHuesped, String apellidoHuesped, String tipoDoc,String numDoc){
        return huespedDAO.buscarDatos(nombreHuesped,apellidoHuesped,tipoDoc,numDoc);
    }

    /**
     * valida q sea el huesped mayor de edad
     * @param huespedDTO
     * @return
     */
    public boolean esMayorDeEdad(HuespedDTO huespedDTO) {
        Date fechaNacimiento = huespedDTO.getFechaNacimiento();

        Calendar hoy = Calendar.getInstance();
        Calendar nacimiento = Calendar.getInstance();
        nacimiento.setTime(fechaNacimiento);

        int edad = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR);

        // Si todavía no cumplió , restamos 1 porq todavia no complio años pero en la resta da 18
        if (hoy.get(Calendar.MONTH) < nacimiento.get(Calendar.MONTH) ||
                ((hoy.get(Calendar.MONTH) == nacimiento.get(Calendar.MONTH) &&
                        hoy.get(Calendar.DAY_OF_MONTH) < nacimiento.get(Calendar.DAY_OF_MONTH)))){
            edad--;
        }

        return edad >= 18;
    }

}
