package com.hotelPremier.classes.medioPago_pago;


import com.hotelPremier.classes.medioDePago.MedioDePago;
import com.hotelPremier.classes.pago.Pago;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Table
@Entity(name="mediopago_pago")
public class Mediopago_pago {

    @EmbeddedId
    Mediopago_pagoId mediopago_pagoid;

    @ManyToOne
    @JoinColumn(name="id_pago")
    Pago pago;

    @ManyToOne
    @JoinColumn(name="id_mediodepago")
    MedioDePago mediodepago;

    


    public void setIdPago(Integer id_pago){
        this.mediopago_pagoid.setIdPago(id_pago);
    }
    public void setIdMedioDePago(Integer id_medioDePago){
        this.mediopago_pagoid.setIdMedioDePago(id_medioDePago);
    }

    public Integer getIdPago(){
        return this.mediopago_pagoid.getIdPago()
    }
    public Integer getIdMedioDePago(){
        return this.mediopago_pagoid.getIdMedioDePago()
    }
}
