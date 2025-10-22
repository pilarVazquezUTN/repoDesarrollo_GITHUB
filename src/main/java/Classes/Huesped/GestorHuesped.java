package Classes.Huesped;

public class GestorHuesped {
    private String idEmpleado;

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
    public void registrarHuesped(HuespedDAO huespedDAO, HuespedDTO huespedDTO) {
        /*
         llamar a la funcion del dao para que guarde los datos.
         */
        huespedDAO.registrarHuesped(huespedDTO);
    }


}
