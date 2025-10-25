package classes.habitacion;

public class GestorHabitacion {
    private HabitacionDAO habitacionDAO;
    private String idEmpleado;

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }
    public String getIdEmpleado() {
        return this.idEmpleado;
    }
    public void muestraEstado(){
        habitacionDAO.muestraEstado();
    }
    void modificarEstado(){

    }
}
