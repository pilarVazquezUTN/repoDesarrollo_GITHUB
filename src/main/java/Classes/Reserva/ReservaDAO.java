package Classes.Reserva;
 
public class ReservaDAO implements ReservaDAOInterfaz {
    private static ReservaDAO instancia; // única instancia

    private ReservaDAO() { }

    /**
     *
     * @return
     */
    public static synchronized ReservaDAO getInstancia() {
        if (instancia == null) {
            instancia = new ReservaDAO();
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
