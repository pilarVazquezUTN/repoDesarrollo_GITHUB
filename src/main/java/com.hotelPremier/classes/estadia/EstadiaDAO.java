package classes.estadia;

public class EstadiaDAO implements EstadiaDAOInterfaz {
    private static EstadiaDAO instancia; // única instancia

    private EstadiaDAO() { }

    /**
     * Devuelve la única instancia de EstadiaDAO.
     * Si todavía no fue creada, la instancia se genera y se guarda.
     *
     * @return instancia única de EstadiaDAO
     */
    public static synchronized EstadiaDAO getInstancia() {
        if (instancia == null) {
            instancia = new EstadiaDAO();
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
