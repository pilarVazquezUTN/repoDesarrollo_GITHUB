package com.hotelPremier.classes.habitacion;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SuperiorFamilyPlan")
public class SuperiorFamilyPlan extends Habitacion{
    private Integer camasIndividuales;
    private Integer camaDoble;
}
