package Classes.ResponsablePago;

public class PersonaJuridicaDAO implements PersonaJuridicaDAOInterfaz {
    private static PersonaJuridicaDAO instancia; // única instancia
    private PersonaJuridicaDAO() { }

    /**
     *
     * @return
     */
    public static synchronized PersonaJuridicaDAO getInstancia() {
        if (instancia == null) {
            instancia = new PersonaJuridicaDAO();
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
