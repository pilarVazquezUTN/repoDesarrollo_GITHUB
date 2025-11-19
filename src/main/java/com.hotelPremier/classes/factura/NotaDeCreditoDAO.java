/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes.Factura;

/**
 *
 * @author st
 */
public class NotaDeCreditoDAO implements NotaDeCreditoDAOInterfaz {
    private static NotaDeCreditoDAO instancia; // única instancia

    private NotaDeCreditoDAO() { }

    /**
     * Devuelve la única instancia de NotaDeCreditoDAO.
     * Si todavía no fue creada, la instancia se genera y se guarda.
     *
     * @return instancia única de NotaDeCreditoDAO
     */
    public static synchronized NotaDeCreditoDAO getInstancia() {
        if (instancia == null) {
            instancia = new NotaDeCreditoDAO();
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
