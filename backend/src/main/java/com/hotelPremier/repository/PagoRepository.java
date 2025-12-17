package com.hotelPremier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotelPremier.classes.Dominio.Pago;
import com.hotelPremier.classes.Dominio.Factura;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {
        boolean existsByFactura(Factura factura);

}
