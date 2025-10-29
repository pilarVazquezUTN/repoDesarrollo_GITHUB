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
    public static synchronized PagoDAO getInstancia() {
        if (instancia == null) {
            instancia = new PagoDAO();
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
