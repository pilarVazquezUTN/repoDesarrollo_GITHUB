/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes.Factura;

/**
 *
 * @author st
 */
public class FacturaDAO implements FacturaDAOInterfaz {
    private static FacturaDAO instancia; // única instancia

    private FacturaDAO() { }

    public static synchronized FacturaDAO getInstancia() {
        if (instancia == null) {
            instancia = new FacturaDAO();
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
