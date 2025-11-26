package com.hotelPremier.classes.habitacion;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("DobleSuperior")
public class DobleSuperior  extends Habitacion{
    private boolean camasIndividuales;
    private boolean camaDoble;
}
