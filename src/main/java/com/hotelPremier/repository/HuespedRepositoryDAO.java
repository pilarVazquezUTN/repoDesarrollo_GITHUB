package com.hotelPremier.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hotelPremier.classes.huesped.Huesped;
import com.hotelPremier.classes.huesped.HuespedDTO;
import com.hotelPremier.classes.huesped.HuespedID;

@Repository
public interface HuespedRepositoryDAO extends JpaRepository<Huesped,HuespedID>{

    @Override
    List<Huesped> findAll();
    List<Huesped> findByHuespedID_Dni(String dni);
    //HuespedDTO addHuesped(HuespedDTO huespedDTO);  
    Huesped save(Huesped huesped);
}
