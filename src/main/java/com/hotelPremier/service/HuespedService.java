package com.hotelPremier.service;

import com.hotelPremier.classes.DTO.HuespedDTO;
import com.hotelPremier.classes.Dominio.Direccion;
import com.hotelPremier.classes.Dominio.Huesped;
import com.hotelPremier.classes.Dominio.HuespedID;
import com.hotelPremier.classes.mapper.ClassMapper;
import com.hotelPremier.repository.DireccionRepository;
import com.hotelPremier.repository.HuespedRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HuespedService {

    @Autowired
    private HuespedRepository huespedRepository;

    @Autowired
    private ClassMapper mapper;

    @Autowired
    private DireccionRepository direccionRepository;

    /**
     * Obtiene todos los huéspedes registrados.
     */
    public List<HuespedDTO> findAll() {
        return mapper.toDtos(huespedRepository.findAll());
    }

    /**
     * Busca huéspedes por DNI.
     */
    public List<HuespedDTO> findByCategory(String dni) {
        return mapper.toDtos(
                huespedRepository.findByHuespedID_Dni(dni)
        );
    }

    /**
     * Busca huéspedes por múltiples criterios.
     */
    public List<HuespedDTO> buscarHuespedes(
            String dni,
            String nombre,
            String apellido,
            String tipoDocumento
    ) {
        List<Huesped> lista = huespedRepository.buscarHuespedes(
                dni, nombre, apellido, tipoDocumento
        );
        return mapper.toDtos(lista);
    }

    /**
     * Registra un nuevo huésped junto con su dirección.
     */
    public HuespedDTO addHuesped(HuespedDTO huespedDTO) {

        Huesped huesped = mapper.toEntity(huespedDTO);
        Direccion direccion = huesped.getDireccion();

        direccionRepository.save(direccion);
        Huesped saved = huespedRepository.save(huesped);

        return mapper.toDTO(saved);
    }

    /**
     * Elimina un huésped por tipo y número de documento.
     */
    public void deleteHuesped(String tipoDocumento, String dni) {

        HuespedID id = new HuespedID();
        id.setTipoDocumento(tipoDocumento);
        id.setDni(dni);

        if (!huespedRepository.existsById(id)) {
            throw new RuntimeException("Huesped no encontrado");
        }

        huespedRepository.deleteById(id);
    }
}
