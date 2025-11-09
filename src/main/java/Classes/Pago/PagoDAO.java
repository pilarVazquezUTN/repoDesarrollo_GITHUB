/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes.Pago;

/**
 *
 * @author st
 */
public class PagoDAO implements PagoDAOInterfaz {
    private static PagoDAO instancia; // única instancia
    private PagoDAO() { }

    /**
     * Devuelve la única instancia de PagoDAO.
     * Si todavía no fue creada, la instancia se genera y se guarda.
     *
     * @return instancia única de PagoDAO
     */
    public static synchronized PagoDAO getInstancia() {
        if (instancia == null) {
            instancia = new PagoDAO();
        }
        return instancia;
    }

    /**
     * Elimina un elemento de la base de datos.
     */
    public void delete(){

    }

    /**
     * Crea un nuevo elemento de la base de datos.
     */
    public  void create(){
    }

    /**
     * Actualiza un elemento de la base de datos.
     */
    public  void update(){
    }

    /**
     * Lee un elemento de la base de datos.
     */
    public  void read(){
    }
}
