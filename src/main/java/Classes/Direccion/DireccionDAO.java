package Classes.Direccion;

public class DireccionDAO implements DireccionDAOInterfaz {
    private static DireccionDAO instancia; // única instancia

    private DireccionDAO() { }

    public static synchronized DireccionDAO getInstancia() {
        if (instancia == null) {
            instancia = new DireccionDAO();
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
