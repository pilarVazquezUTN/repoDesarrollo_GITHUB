package Classes.MedioDePago;

import java.util.Date;

public class ChequeDTO {
    private int numeroCheque;
    private String banco;
    private Date plazo;

    /**
     *
     * @param numeroCheque
     */
    public void setNumeroCheque(int numeroCheque) {
        this.numeroCheque = numeroCheque;
    }

    /**
     *
     * @return
     */
    public int getNumeroCheque() {
        return numeroCheque;
    }

    /**
     *
     * @param banco
     */
    public void setBanco(String banco) {
        this.banco = banco;
    }

    /**
     *
     * @return
     */
    public String getBanco() {
        return banco;
    }

    /**
     *
     * @param plazo
     */
    public void setPlazo(Date plazo) {
        this.plazo = plazo;
    }

    /**
     *
     * @return
     */
    public Date getPlazo() {
        return plazo;
    }    
}
