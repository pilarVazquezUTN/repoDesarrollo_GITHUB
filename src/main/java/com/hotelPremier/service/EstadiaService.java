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

    // ============================================
    // INICIAR ESTADIA CON PATRÓN OBSERVER
    // ============================================
    /**
     * Inicia una estadía registrando los observers y notificándolos cuando cambia a ENCURSO.
     * 
     * Los observers registrados son:
     * - ActualizarHabitacionObserver: cambia la habitación a OCUPADA
     * - ActualizarReservaObserver: cambia la reserva a CONSUMIDA
     * 
     * @param estadia La estadía a iniciar
     * @return La estadía iniciada
     */
    public Estadia iniciarEstadia(Estadia estadia) {
        // Preparar estadía para inicio: registrar observers que reaccionarán al cambio de estado
        prepararEstadiaParaInicio(estadia);

        // Iniciar la estadía (cambia a ENCURSO y notifica a los observers)
        estadia.iniciarEstadia();

        // Persistir los cambios (habitación y reserva fueron actualizados por los observers)
        estadiaRepository.save(estadia);
        
        // Persistir la habitación si fue actualizada
        if (estadia.getHabitacion() != null) {
            // La habitación se persiste automáticamente si está en el contexto de persistencia
            // Si no, necesitaríamos el repositorio de habitación
        }

        return estadia;
    }

    /**
     * Prepara la estadía para inicio registrando los observers necesarios.
     * El registro de observers está claramente separado del cambio de estado.
     * 
     * @param estadia La estadía a preparar
     */
    private void prepararEstadiaParaInicio(Estadia estadia) {
        estadia.registrarObserver(new ActualizarHabitacionObserver());
        estadia.registrarObserver(new ActualizarReservaObserver());
    }
}
