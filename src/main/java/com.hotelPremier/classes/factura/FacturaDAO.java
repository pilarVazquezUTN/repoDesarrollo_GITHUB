/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes.factura;

/**
 *
 * @author st
 */
public class FacturaDAO implements FacturaDAOInterfaz {
    private static FacturaDAO instancia; // única instancia

    private FacturaDAO() { }

    /**
     * Devuelve la única instancia de FacturaDAO.
     * Si todavía no fue creada, la instancia se genera y se guarda.
     *
     * @return instancia única de FacturaDAO
     */
    public static synchronized FacturaDAO getInstancia() {
        if (instancia == null) {
            instancia = new FacturaDAO();
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
