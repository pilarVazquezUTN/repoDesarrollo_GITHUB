package com.hotelPremier.classes.direccion;

public class DireccionDAO implements DireccionDAOInterfaz {
    private static DireccionDAO instancia; // única instancia

    private DireccionDAO() { }

    /**
     * Devuelve la única instancia de DireccionDAO.
     * Si todavía no fue creada, la instancia se genera y se guarda.
     *
     * @return instancia única de DireccionDAO
     */
    public static synchronized DireccionDAO getInstancia() {
        if (instancia == null) {
            instancia = new DireccionDAO();
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
