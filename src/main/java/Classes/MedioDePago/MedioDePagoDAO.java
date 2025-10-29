/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes.MedioDePago;

import Classes.Huesped.HuespedDAO;

/**
 *
 * @author st
 */
public class MedioDePagoDAO implements MedioDePagoDAOInterfaz {
    private static MedioDePagoDAO instancia; // única instancia

    private MedioDePagoDAO() { }

    public static synchronized MedioDePagoDAO getInstancia() {
        if (instancia == null) {
            instancia = new MedioDePagoDAO();
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
