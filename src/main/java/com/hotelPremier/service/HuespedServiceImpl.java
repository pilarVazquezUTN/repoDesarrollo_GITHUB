package com.hotelPremier.service;

import com.hotelPremier.classes.huesped.Huesped;
import com.hotelPremier.classes.huesped.HuespedDTO;
import com.hotelPremier.classes.mapper.ClassMapper;
import com.hotelPremier.repository.HuespedRepositoryDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HuespedServiceImpl {

    @Autowired
    private HuespedRepositoryDAO huespedRepository;

    @Autowired
    private ClassMapper mapper;


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


    // ============================
    // 4) AGREGAR HUESPED
    // ============================
    public HuespedDTO addHuesped(HuespedDTO dto) {

        // MapStruct convierte DTO → Entity
        Huesped entity = mapper.toEntity(dto);

        // Guardar en DB
        Huesped saved = huespedRepository.save(entity);

        // Convertimos de vuelta a DTO
        return mapper.toDTO(saved);
    }
    public void deleteHuesped(String dni) { }

}