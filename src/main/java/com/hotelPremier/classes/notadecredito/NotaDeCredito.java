
package com.hotelPremier.classes.notadecredito;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="notaDeCredito")
public class NotaDeCredito {
    @Id
    @Column(name="id_notaCredito")
    private Integer id_notadecredito;

    @Column(name="monto")
    private float monto;





}
