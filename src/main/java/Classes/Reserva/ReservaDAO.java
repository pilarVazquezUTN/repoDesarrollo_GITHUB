package Classes.Reserva;
 
public class ReservaDAO implements ReservaDAOInterfaz {
    private static ReservaDAO instancia; // única instancia

    private ReservaDAO() { }

    /**
     * Devuelve la única instancia de ReservaDAO.
     * Si todavía no fue creada, la instancia se genera y se guarda.
     *
     * @return instancia única de ReservaDAO
     */
    public static synchronized ReservaDAO getInstancia() {
        if (instancia == null) {
            instancia = new ReservaDAO();
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
