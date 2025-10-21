/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes.MedioDePago;

/**
 *
 * @author st
 */
public class TarjetaCredito {
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
