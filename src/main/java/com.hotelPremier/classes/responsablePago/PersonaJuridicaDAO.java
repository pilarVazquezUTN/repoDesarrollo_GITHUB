package classes.responsablePago;

public class PersonaJuridicaDAO implements PersonaJuridicaDAOInterfaz {
    private static PersonaJuridicaDAO instancia; // única instancia
    private PersonaJuridicaDAO() { }

    /**
     * Devuelve la única instancia de PersonaJuridicaDAO.
     * Si todavía no fue creada, la instancia se genera y se guarda.
     *
     * @return instancia única de PersonaJuridicaDAO
     */
    public static synchronized PersonaJuridicaDAO getInstancia() {
        if (instancia == null) {
            instancia = new PersonaJuridicaDAO();
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
