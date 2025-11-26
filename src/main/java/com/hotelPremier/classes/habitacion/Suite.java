package com.hotelPremier.classes.habitacion;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Suite")
public class Suite extends Habitacion{
    private boolean camasKingSize;
}
