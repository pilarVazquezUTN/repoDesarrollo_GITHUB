package com.hotelPremier.classes.relacionEstadiaHuesped;

import com.hotelPremier.classes.estadia.Estadia;
import com.hotelPremier.classes.huesped.Huesped;
import jakarta.persistence.*;


@Entity
@Table(name="estadiahuesped")
public class estadiaHuesped {

  @EmbeddedId
  estadiaHuespedID idEstadiaHuesped;

  @ManyToOne
  @MapsId("idEstadia")
  @JoinColumn(name = "id_estadia")
  private Estadia estadia;

  @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "dni", referencedColumnName = "dni"),
            @JoinColumn(name = "tipoDocumento", referencedColumnName = "tipoDocumento")
    })
    private Huesped huesped;

}
