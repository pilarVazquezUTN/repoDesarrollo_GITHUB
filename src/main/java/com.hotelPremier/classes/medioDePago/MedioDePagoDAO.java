/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes.medioDePago;

import classes.huesped.HuespedDAO;

/**
 *
 * @author st
 */
public class MedioDePagoDAO implements MedioDePagoDAOInterfaz {
    private static MedioDePagoDAO instancia; // única instancia

    private MedioDePagoDAO() { }

    /**
     * Devuelve la única instancia de MedioDePagoDAO.
     * Si todavía no fue creada, la instancia se genera y se guarda.
     *
     * @return instancia única de MedioDePagoDAO
     */
    public static synchronized MedioDePagoDAO getInstancia() {
        if (instancia == null) {
            instancia = new MedioDePagoDAO();
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
