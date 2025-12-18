package com.hotelPremier.service;

import com.hotelPremier.classes.DTO.ReservaDTO;
import com.hotelPremier.classes.Dominio.Estadia;
import com.hotelPremier.classes.Dominio.Habitacion;
import com.hotelPremier.classes.Dominio.Reserva;
import com.hotelPremier.classes.exception.NegocioException;
import com.hotelPremier.classes.exception.RecursoNoEncontradoException;
import com.hotelPremier.classes.mapper.ClassMapper;
import com.hotelPremier.repository.EstadiaRepository;
import com.hotelPremier.repository.HabitacionRepository;
import com.hotelPremier.repository.ReservaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private EstadiaRepository estadiaRepository;

    @Autowired
    private EstadiaService estadiaService;

    @Autowired
    private ClassMapper mapper;

    /**
     * Registra una lista de reservas validando habitación y fechas.
     */
    public List<ReservaDTO> guardarLista(List<ReservaDTO> listaDTO) {

        List<Reserva> entidades = new ArrayList<>();

        for (ReservaDTO dto : listaDTO) {

            if (dto.getHabitacion() == null)
                throw new IllegalArgumentException("El DTO de reserva no contiene la habitación.");

            Integer nro = dto.getHabitacion().getNumero();
            if (nro == null)
                throw new IllegalArgumentException("El DTO no trae número de habitación.");

            Habitacion hab = habitacionRepository.findById(nro)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Habitación no encontrada: " + nro));

            Integer solapa = reservaRepository.haySuperposicion(
                    nro,
                    dto.getFecha_desde(),
                    dto.getFecha_hasta()
            );

            if (solapa != null && solapa > 0)
                throw new NegocioException("La habitación " + nro + " ya tiene reservas en ese rango de fechas.");

            Reserva r = mapper.toEntityReserva(dto);
            if (r.getEstado() == null || r.getEstado().trim().isEmpty()) {
                r.setEstado("PENDIENTE");
            }

            r.setHabitacion(hab);
            entidades.add(r);
        }

        reservaRepository.saveAll(entidades);
        return mapper.toDtosReserva(entidades);
    }

    /**
     * Busca reservas dentro de un rango de fechas.
     */
    public List<ReservaDTO> buscarEntreFechas(LocalDate desde, LocalDate hasta) {
        List<Reserva> reservas = reservaRepository.buscarEntreFechas(desde, hasta);
        return mapper.toDtosReserva(reservas);
    }

    /**
     * Busca reservas por apellido y/o nombre.
     */
    public List<ReservaDTO> buscarPorApellidoNombre(String apellido, String nombre) {

        boolean tieneApe = apellido != null && !apellido.trim().isEmpty();
        boolean tieneNom = nombre != null && !nombre.trim().isEmpty();

        List<Reserva> lista;

        if (tieneApe && tieneNom) {
            lista = reservaRepository
                    .findByApellidoStartingWithIgnoreCaseAndNombreStartingWithIgnoreCase(apellido, nombre);

        } else if (tieneApe) {
            lista = reservaRepository.findByApellidoStartingWithIgnoreCase(apellido);

        } else if (tieneNom) {
            lista = reservaRepository.findByNombreStartingWithIgnoreCase(nombre);

        } else {
            lista = reservaRepository.findAll();
        }

        return mapper.toDtosReserva(lista);
    }

    /**
     * Cancela una reserva cambiando su estado a CANCELADA.
     * Solo permite cancelar reservas en estado PENDIENTE.
     */
    public ReservaDTO cancelarReserva(Integer idReserva) {

        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reserva no encontrada con ID: " + idReserva));

        // Validar que la reserva esté en estado PENDIENTE
        String estadoActual = reserva.getEstado();
        if (estadoActual == null || estadoActual.trim().isEmpty()) {
            estadoActual = "PENDIENTE"; // Estado por defecto
        }
        
        if (!estadoActual.equalsIgnoreCase("PENDIENTE")) {
            throw new NegocioException(
                "Solo se pueden cancelar reservas en estado PENDIENTE. La reserva actual está en estado: " + estadoActual
            );
        }

        reserva.cancelar();
        reservaRepository.save(reserva);

        return mapper.toDTOReserva(reserva);
    }

    /**
     * Realiza el check-in de una reserva y crea la estadía asociada.
     */
    public Estadia hacerCheckIn(Integer idReserva) {

        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reserva no encontrada con ID: " + idReserva));

        Estadia estadia = reserva.checkIn();
        estadia = estadiaService.iniciarEstadia(estadia);

        reservaRepository.save(reserva);
        estadiaRepository.save(estadia);

        if (estadia.getHabitacion() != null) {
            habitacionRepository.save(estadia.getHabitacion());
        }

        return estadia;
    }
}
