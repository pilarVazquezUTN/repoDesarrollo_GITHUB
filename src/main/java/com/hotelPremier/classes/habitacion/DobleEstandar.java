package com.hotelPremier.classes.habitacion;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("DobleEstandar")
public class DobleEstandar  extends Habitacion {
    private boolean camasIndividuales;
    private boolean camaDoble;
}
