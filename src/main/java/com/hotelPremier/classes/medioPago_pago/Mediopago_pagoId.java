package com.hotelPremier.classes.medioPago_pago;

import com.hotelPremier.classes.huesped.HuespedID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Mediopago_pagoId {

    @Column(name="id_pago")
    Integer id_pago;
    @Column(name="id_mediodepago")
    Integer id_mediodepago;


    public void setIdPago(Integer id_pago){
        this.id_pago=id_pago;
    }
    public void setIdMedioDePago(Integer id_medioDePago){
        this.id_mediodepago=id_medioDePago;
    }

    public Integer getIdPago(){
        return this.id_pago;
    }
    public Integer getIdMedioDePago(){
        return this.id_mediodepago;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mediopago_pagoId that = (Mediopago_pagoId) o;
        return id_pago.equals(that.id_pago) && id_mediodepago.equals(that.id_mediodepago);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id_pago, id_mediodepago);
    }    
}
