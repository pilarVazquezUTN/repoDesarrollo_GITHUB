package com.hotelPremier.service;

import com.hotelPremier.classes.DTO.EstadiaDTO;
import com.hotelPremier.classes.Dominio.Estadia;
import com.hotelPremier.classes.Dominio.estadia.observer.ActualizarHabitacionObserver;
import com.hotelPremier.classes.Dominio.estadia.observer.ActualizarReservaObserver;
import com.hotelPremier.classes.Dominio.servicioExtra.ServicioExtra;
import com.hotelPremier.classes.mapper.ClassMapper;
import com.hotelPremier.repository.EstadiaRepository;
import com.hotelPremier.repository.ServicioExtraRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstadiaService {

    @Autowired
    private EstadiaRepository estadiaRepository;

    @Autowired
    private ServicioExtraRepository servicioExtraRepository;

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
     * Incluye los huéspedes asociados y los consumos (servicios extra).
     */
    public EstadiaDTO obtenerEstadiaEnCurso(Integer nroHabitacion) {
        Estadia estadia = estadiaRepository.estadiaEnCurso(nroHabitacion);
        if (estadia == null) return null;
        
        EstadiaDTO dto = mapper.toDTOsEstadia(List.of(estadia)).get(0);
        
        // Cargar y agregar los consumos (servicios extra) asociados a la estadía
        if (estadia.getId_estadia() != null) {
            List<ServicioExtra> consumos = 
                servicioExtraRepository.findByEstadiaId(estadia.getId_estadia());
            dto.setListaconsumos(mapper.toDTOsServicioExtra(consumos));
        }
        
        return dto;
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
