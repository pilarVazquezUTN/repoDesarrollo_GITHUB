package Classes.Estadia;

public class EstadiaDAO implements EstadiaDAOInterfaz {
    private static EstadiaDAO instancia; // única instancia

    private EstadiaDAO() { }

    public static synchronized EstadiaDAO getInstancia() {
        if (instancia == null) {
            instancia = new EstadiaDAO();
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
