package com.hotelPremier.classes.relacionEstadiaHuesped;

import com.hotelPremier.classes.estadia.Estadia;
import com.hotelPremier.classes.huesped.Huesped;
import jakarta.persistence.*;


@Entity
@Table(name="\"estadiaHuesped\"")
public class estadiaHuesped {

  @EmbeddedId
  estadiaHuespedID id_estadia;

  @ManyToOne
  @MapsId("idEstadia")
  @JoinColumn(name = "id_estadia")
  private Estadia estadia;

  @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "dni", referencedColumnName = "dni", insertable=false, updatable=false),
            @JoinColumn(name = "tipoDocumento", referencedColumnName = "tipoDocumento",insertable=false, updatable=false)
    })
    private Huesped huesped;

    public estadiaHuespedID getId_estadia() {
        return id_estadia;
    }

    public void setId_estadia(estadiaHuespedID id_estadia) {
        this.id_estadia = id_estadia;
    }

    public Estadia getEstadia() {
        return estadia;
    }

    public void setEstadia(Estadia estadia) {
        this.estadia = estadia;
    }

    public Huesped getHuesped() {
        return huesped;
    }

    public void setHuesped(Huesped huesped) {
        this.huesped = huesped;
    }

}
