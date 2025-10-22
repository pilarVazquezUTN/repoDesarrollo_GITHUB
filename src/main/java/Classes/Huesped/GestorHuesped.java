package Classes.Huesped;

import java.io.IOException;

public class GestorHuesped {
    private String idEmpleado;
    HuespedDAO huespedDAO = new HuespedDAO();

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }
    public String getIdEmpleado() {
        return idEmpleado;
    }



    public void  buscarHuesped(){

    } 
    public Huesped darAltaHuesped(){
        return null;
    }
    public void darBajaHuesped(){

    }
    public void modificarHuesped(HuespedDTO huespedDTO) {
        
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

}
