package classes.medioDePago;

public class TarjetaCreditoDTO {
    private String banco;
    private int cuotas;

    public void setBanco(String banco) {
        this.banco = banco;
    }
    public String getBanco() {

        return banco;

    }
    public void setCuotas(int cuotas) {
        this.cuotas = cuotas;
    }
    public int getCuotas() {
        return cuotas;
    }    
}
