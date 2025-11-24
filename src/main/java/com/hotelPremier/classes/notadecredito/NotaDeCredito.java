
package com.hotelPremier.classes.notadecredito;

import java.util.List;

import com.hotelPremier.classes.factura.Factura;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="notadecredito")
public class NotaDeCredito {
    @Id
    @Column(name="id_notacredito")
    private Integer id_notadecredito;

    @Column(name="monto")
    private float monto;

    @OneToMany(mappedBy = "notacredito")
    private List<Factura> listafactura;
}
