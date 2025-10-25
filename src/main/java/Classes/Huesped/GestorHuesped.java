package classes.huesped;

import java.io.IOException;

public class GestorHuesped {
    private String idEmpleado;
    HuespedDAO huespedDAO = new HuespedDAO();

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

    /**
     *
     * @param huespedDTO datos
     * @param huespedDAO instancia del Dao
     * @param rutaArchivo ruta donde esta el archivo
     *
     *
     *el gestor le pasa al dao los datos y la ruta donde tiene q buscar
     */
    public void modificarHuesped(HuespedDTO huespedDTO, String rutaArchivo, String dninoMod) {
        huespedDAO.actualizarHuesped(rutaArchivo, huespedDTO, huespedDTO.getDireccionHuesped(), dninoMod);
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

}
