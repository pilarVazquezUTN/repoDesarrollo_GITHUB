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
     * retorna una instancia
     * @return
     */
    public static synchronized NotaDeCreditoDAO getInstancia() {
        if (instancia == null) {
            instancia = new NotaDeCreditoDAO();
        }
        return instancia;
    }
    public void delete(){

    }
    public  void create(){
    }
    public  void update(){
    }
    public  void read(){
    }
}
