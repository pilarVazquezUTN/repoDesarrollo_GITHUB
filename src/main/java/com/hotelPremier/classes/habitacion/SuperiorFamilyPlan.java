package com.hotelPremier.classes.habitacion;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SuperiorFamilyPlan")
public class SuperiorFamilyPlan extends Habitacion{
    @Column(name = "camasindividuales")
    private Integer camasIndividuales;
    @Column(name = "camadoble")
    private Integer camaDoble;
}
