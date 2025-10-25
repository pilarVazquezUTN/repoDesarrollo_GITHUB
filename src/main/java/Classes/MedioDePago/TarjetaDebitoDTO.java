package classes.medioDePago;

public class TarjetaDebitoDTO {
    private String banco;
    private String dniTitular;


    public String getBanco() {

        return banco;
    }
    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getDniTitular() {
        return dniTitular;
    }
    public void setDniTitular(String dniTitular) {
        this.dniTitular = dniTitular;
    }
}
