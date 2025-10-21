/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.util.Date;

/**
 *
 * @author st
 */
public class Cheque {
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
