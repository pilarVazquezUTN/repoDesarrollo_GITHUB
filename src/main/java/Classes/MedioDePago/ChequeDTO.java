package classes.medioDePago;

import java.util.Date;

public class ChequeDTO {
    private int numeroCheque;
    private String banco;
    private Date plazo;
    
    public void setNumeroCheque(int numeroCheque) {
        this.numeroCheque = numeroCheque;
    }
    public int getNumeroCheque() {
        return numeroCheque;
    }
    public void setBanco(String banco) {
        this.banco = banco;
    }
    public String getBanco() {
        return banco;
    }
    public void setPlazo(Date plazo) {
        this.plazo = plazo;
    }
    public Date getPlazo() {
        return plazo;
    }    
}
