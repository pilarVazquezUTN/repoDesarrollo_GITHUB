package com.hotelPremier.classes.habitacion;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("IndividualEstandar")
public class IndividualEstandar  extends Habitacion{
    @Column(name = "camaskingsize")
    private Integer camasKingSize;
}
