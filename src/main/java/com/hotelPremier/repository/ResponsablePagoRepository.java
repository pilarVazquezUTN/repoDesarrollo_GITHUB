package com.hotelPremier.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotelPremier.classes.Dominio.responsablePago.*;

@Repository
public interface ResponsablePagoRepository extends JpaRepository<ResponsablePago, Integer> {}
