package com.hotelPremier.service;

import com.hotelPremier.classes.DTO.HuespedDTO;
import com.hotelPremier.classes.Dominio.Direccion;
import com.hotelPremier.classes.Dominio.Huesped;
import com.hotelPremier.classes.Dominio.HuespedID;
import com.hotelPremier.classes.mapper.ClassMapper;
import com.hotelPremier.repository.DireccionRepository;
import com.hotelPremier.repository.HuespedRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class HuespedService {

    @Autowired
    private HuespedRepository huespedRepository;

    @Autowired
    private ClassMapper mapper;

    @Autowired
    private DireccionRepository direccionRepository;


    // ============================
    // 1) OBTENER TODOS LOS HUESPEDES
    // ============================
    public List<HuespedDTO> findAll() {
        return mapper.toDtos(huespedRepository.findAll());
    }


    // ============================
    // 2) BUSCAR SOLO POR DNI (si lo necesitás)
    // ============================
    public List<HuespedDTO> findByCategory(String dni) {
        return mapper.toDtos(
                huespedRepository.findByHuespedID_Dni(dni)
        );
    }


    // ============================
    // 3) BÚSQUEDA MULTIPARÁMETRO (principal)
    // ============================
    public List<HuespedDTO> buscarHuespedes(
            String dni,
            String nombre,
            String apellido,
            String tipoDocumento
    ) {

        // El repositorio devuelve List<Huesped>
        List<Huesped> lista = huespedRepository.buscarHuespedes(
                dni, nombre, apellido, tipoDocumento
        );

        // MapStruct convierte a DTO
        return mapper.toDtos(lista);
    }


    public HuespedDTO addHuesped(HuespedDTO huespedDTO) {

        
        // MapStruct convierte DTO → Entity
        Huesped huesped = mapper.toEntity(huespedDTO);
        Direccion direccion = huesped.getDireccion();
        
        direccionRepository.save(direccion);

        // Guardar en DB
        Huesped saved = huespedRepository.save(huesped);

        // Convertimos de vuelta a DTO
        return mapper.toDTO(saved);
    }
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