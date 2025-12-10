package com.hotelPremier.service;

import com.hotelPremier.classes.DTO.EstadiaDTO;
import com.hotelPremier.classes.Dominio.Estadia;
import com.hotelPremier.classes.mapper.ClassMapper;
import com.hotelPremier.repository.EstadiaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstadiaService {

    @Autowired
    private EstadiaRepository estadiaRepository;

    @Autowired
    private ClassMapper mapper;

    // ============================================
    // LISTAR ESTADIAS SIN FACTURA
    // ============================================
    public List<EstadiaDTO> obtenerEstadiasSinFactura() {

        List<Estadia> lista = estadiaRepository.estadiasSinFactura();

        return mapper.toDTOsEstadia(lista);
    }

    public EstadiaDTO obtenerEstadiaEnCurso(Integer nroHabitacion) {
        Estadia estadia = estadiaRepository.estadiaEnCurso(nroHabitacion);
        if (estadia == null) System.out.println("No se encontro una estadia para esta habitacion " + nroHabitacion);
        if (estadia == null) return null;
        System.err.println("Si se encontro una estadia");

        return mapper.toDTOsEstadia(java.util.List.of(estadia)).get(0);
    }
}
