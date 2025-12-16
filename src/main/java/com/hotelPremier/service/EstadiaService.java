package com.hotelPremier.service;

import com.hotelPremier.classes.DTO.EstadiaDTO;
import com.hotelPremier.classes.Dominio.Estadia;
import com.hotelPremier.classes.Dominio.estadia.observer.ActualizarHabitacionObserver;
import com.hotelPremier.classes.Dominio.estadia.observer.ActualizarReservaObserver;
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

    /**
     * Obtiene las estadías que aún no tienen factura asociada.
     */
    public List<EstadiaDTO> obtenerEstadiasSinFactura() {
        List<Estadia> lista = estadiaRepository.estadiasSinFactura();
        return mapper.toDTOsEstadia(lista);
    }

    /**
     * Obtiene la estadía en curso de una habitación.
     */
    public EstadiaDTO obtenerEstadiaEnCurso(Integer nroHabitacion) {
        Estadia estadia = estadiaRepository.estadiaEnCurso(nroHabitacion);
        if (estadia == null) return null;
        return mapper.toDTOsEstadia(List.of(estadia)).get(0);
    }

    /**
     * Inicia una estadía cambiando su estado a ENCURSO.
     */
    public Estadia iniciarEstadia(Estadia estadia) {
        prepararEstadiaParaInicio(estadia);
        estadia.iniciarEstadia();
        estadiaRepository.save(estadia);
        return estadia;
    }

    /**
     * Registra los observers necesarios para iniciar una estadía.
     */
    private void prepararEstadiaParaInicio(Estadia estadia) {
        estadia.registrarObserver(new ActualizarHabitacionObserver());
        estadia.registrarObserver(new ActualizarReservaObserver());
    }
}
