package com.hotelPremier.service;//package com.hotelPremier.service;

import com.hotelPremier.repository.HuespedRepositoryDAO;
import com.hotelPremier.classes.direccion.Direccion;
import org.springframework.stereotype.Service;
import com.hotelPremier.classes.huesped.Huesped;
import com.hotelPremier.classes.huesped.HuespedDTO;
import com.hotelPremier.classes.huesped.HuespedID;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.hotelPremier.classes.FuncionesUtiles;



@Service
public class HuespedServiceImpl{

    FuncionesUtiles funcionesUtiles = new FuncionesUtiles();

    @Autowired
    private HuespedRepositoryDAO huespedRepository;

    public List<HuespedDTO> findAll(){
        /* huespedRepository.findAll() nos devuelve un Huesped. Hay que pasarlo a HuespedDTO */
        return funcionesUtiles.mapToDTOList(huespedRepository.findAll()); 
    }

    
    public List<HuespedDTO> findByDni(String dni) {
        
        return null;
    }

    
    public void deleteHuesped(String dni) {

    }

    
    public HuespedDTO addHuesped(HuespedDTO huespedDTO) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'addHuesped'");
        //huespedRepository.addHuesped(huespedDTO);
        HuespedID id = new HuespedID();
        id.setDni(huespedDTO.getDni());
        id.setTipoDocumento(huespedDTO.getTipoDocumento());
        huespedDTO.setHuespedID(id);
        
        Huesped huesped =  new Huesped();
        Direccion direccion = new Direccion();


        huesped.setNombre(huespedDTO.getNombre());
        huesped.setApellido(huespedDTO.getApellido());
        huesped.setCuit(huespedDTO.getCuit());

        direccion.setID(huespedDTO.getDireccionHuesped().getID());
        direccion.setCalle(huespedDTO.getDireccionHuesped().getCalle());
        direccion.setCodigoPostal(huespedDTO.getDireccionHuesped().getCodigoPostal());
        direccion.setDepartamento(huespedDTO.getDireccionHuesped().getDepartamento());
        direccion.setLocalidad(huespedDTO.getDireccionHuesped().getLocalidad());
        direccion.setNumero(huespedDTO.getDireccionHuesped().getNumero());
        direccion.setPais(huespedDTO.getDireccionHuesped().getPais());
        direccion.setPiso(huespedDTO.getDireccionHuesped().getPiso());
        direccion.setProvincia(huespedDTO.getDireccionHuesped().getProvincia());
        
        huesped.setDni(huespedDTO.getHuespedID().getDni());
        huesped.setTipoDocumento(huespedDTO.getHuespedID().getTipoDocumento());
        huesped.setEmail(huespedDTO.getEmail());
        huesped.setFechaNacimiento(huespedDTO.getFechaNacimiento());
        huesped.setNacionalidad(huespedDTO.getNacionalidad());
        huesped.setOcupacion(huespedDTO.getOcupacion());
        huesped.setPosicionIva(huespedDTO.getPosicionIva());
        huesped.setTelefono(huespedDTO.getTelefono());

        huespedRepository.save(direccion);
        huespedRepository.save(huesped);
        return huespedDTO;
    }

    public List<HuespedDTO> findByCategory(String dni) {
        
        return funcionesUtiles.mapToDTOList(huespedRepository.findByHuespedID_Dni(dni));
    }
}
