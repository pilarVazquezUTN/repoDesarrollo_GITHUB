package com.hotelPremier.service;

import com.hotelPremier.classes.DTO.EstadiaDTO;
import com.hotelPremier.classes.DTO.HuespedDTO;
import com.hotelPremier.classes.Dominio.Estadia;
import com.hotelPremier.classes.Dominio.Habitacion;
import com.hotelPremier.classes.Dominio.Huesped;
import com.hotelPremier.classes.Dominio.HuespedID;
import com.hotelPremier.classes.Dominio.Reserva;
import com.hotelPremier.classes.Dominio.estadia.observer.ActualizarHabitacionObserver;
import com.hotelPremier.classes.Dominio.estadia.observer.ActualizarReservaObserver;
import com.hotelPremier.classes.Dominio.servicioExtra.ServicioExtra;
import com.hotelPremier.classes.exception.NegocioException;
import com.hotelPremier.classes.exception.RecursoNoEncontradoException;
import com.hotelPremier.classes.mapper.ClassMapper;
import com.hotelPremier.repository.EstadiaRepository;
import com.hotelPremier.repository.HabitacionRepository;
import com.hotelPremier.repository.HuespedRepository;
import com.hotelPremier.repository.ReservaRepository;
import com.hotelPremier.repository.ServicioExtraRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class EstadiaService {

    @Autowired
    private EstadiaRepository estadiaRepository;

    @Autowired
    private ServicioExtraRepository servicioExtraRepository;

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private HuespedRepository huespedRepository;

    @Autowired
    private ReservaRepository reservaRepository;

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
        if (estadia == null) {
            throw new RecursoNoEncontradoException(
                "No hay estadía en curso para la habitación " + nroHabitacion
            );
        }
        
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

    /**
     * Agrega una nueva estadía al sistema.
     * Valida que la habitación exista, que no haya otra estadía en curso para esa habitación,
     * y que los huéspedes existan en el sistema.
     */
    @Transactional
    public EstadiaDTO agregarEstadia(EstadiaDTO dto) {
        // Validar que venga la habitación
        if (dto.getHabitacion() == null || dto.getHabitacion().getNumero() == null) {
            throw new NegocioException("Debe especificar el número de habitación.");
        }

        Integer nroHabitacion = dto.getHabitacion().getNumero();

        // Validar que la habitación exista
        Habitacion habitacion = habitacionRepository.findById(nroHabitacion)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la habitación con número: " + nroHabitacion));

        // Validar que no haya otra estadía en curso para esa habitación
        Estadia estadiaExistente = estadiaRepository.estadiaEnCurso(nroHabitacion);
        if (estadiaExistente != null) {
            throw new NegocioException(
                    "Ya existe una estadía en curso para la habitación " + nroHabitacion);
        }

        // Validar que venga la fecha de checkin
        if (dto.getCheckin() == null) {
            throw new NegocioException("Debe especificar la fecha de check-in.");
        }

        // Validar que vengan huéspedes
        if (dto.getListahuesped() == null || dto.getListahuesped().isEmpty()) {
            throw new NegocioException("Debe especificar al menos un huésped.");
        }

        // Buscar y validar que los huéspedes existan
        List<Huesped> huespedes = new ArrayList<>();
        for (HuespedDTO huespedDTO : dto.getListahuesped()) {
            if (huespedDTO.getHuespedID() == null) {
                throw new NegocioException("Cada huésped debe tener su identificación (dni y tipoDocumento).");
            }

            String dni = huespedDTO.getHuespedID().getDni();
            String tipoDoc = huespedDTO.getHuespedID().getTipoDocumento();

            if (dni == null || dni.trim().isEmpty()) {
                throw new NegocioException("El DNI del huésped no puede estar vacío.");
            }
            if (tipoDoc == null || tipoDoc.trim().isEmpty()) {
                throw new NegocioException("El tipo de documento del huésped no puede estar vacío.");
            }

            HuespedID huespedID = new HuespedID();
            huespedID.setDni(dni);
            huespedID.setTipoDocumento(tipoDoc);

            Huesped huesped = huespedRepository.findById(huespedID)
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No existe el huésped con " + tipoDoc + ": " + dni));

            huespedes.add(huesped);
        }

        // Determinar si viene una reserva existente o hay que crear una nueva
        Reserva reserva = null;
        boolean reservaExistente = dto.getReserva() != null && dto.getReserva().getId_reserva() != null;
        
        if (reservaExistente) {
            // CASO 1: Viene una reserva existente - buscarla y validarla
            Integer idReserva = dto.getReserva().getId_reserva();
            reserva = reservaRepository.findById(idReserva)
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No existe la reserva con ID: " + idReserva));
            
            // Validar que la reserva esté en estado PENDIENTE
            if (!"PENDIENTE".equalsIgnoreCase(reserva.getEstado())) {
                throw new NegocioException(
                        "La reserva " + idReserva + " no está en estado PENDIENTE. Estado actual: " + reserva.getEstado());
            }
            // Checkout es opcional cuando viene con reserva
        } else {
            // CASO 2: No viene reserva - el checkout es obligatorio y se crea reserva automática
            if (dto.getCheckout() == null) {
                throw new NegocioException(
                        "Debe especificar la fecha de check-out cuando no se asocia una reserva existente.");
            }
            
            // Crear nueva reserva con los datos de la estadía
            Huesped primerHuesped = huespedes.get(0);
            reserva = new Reserva();
            reserva.setFecha_desde(dto.getCheckin());
            reserva.setFecha_hasta(dto.getCheckout());
            reserva.setNombre(primerHuesped.getNombre());
            reserva.setApellido(primerHuesped.getApellido());
            reserva.setTelefono(primerHuesped.getTelefono());
            reserva.setHabitacion(habitacion);
            reserva.setEstado("FINALIZADA"); // Se crea directamente como FINALIZADA
            
            // Guardar la nueva reserva
            reservaRepository.save(reserva);
        }

        // Crear la nueva estadía
        Estadia estadia = new Estadia();
        estadia.setCheckin(dto.getCheckin());
        estadia.setCheckout(dto.getCheckout()); // Puede ser null si viene con reserva existente
        estadia.setHabitacion(habitacion);
        estadia.setListahuesped(huespedes);
        estadia.setEstado("ENCURSO");
        
        // Asociar la reserva (ya sea existente o recién creada)
        estadia.setReserva(reserva);
        reserva.setEstadia(estadia);

        // Guardar la estadía
        estadiaRepository.save(estadia);
        
        // Si la reserva era existente (estaba en PENDIENTE), finalizarla
        if (reservaExistente) {
            reserva.consumir();
        }
        // Guardar la reserva para actualizar la relación bidireccional
        reservaRepository.save(reserva);

        // Actualizar la relación bidireccional con los huéspedes
        for (Huesped huesped : huespedes) {
            if (huesped.getListaEstadia() == null) {
                huesped.setListaEstadia(new ArrayList<>());
            }
            huesped.getListaEstadia().add(estadia);
            huespedRepository.save(huesped);
        }

        // Actualizar el estado de la habitación a OCUPADA
        habitacion.setEstado("OCUPADA");
        habitacionRepository.save(habitacion);

        // Iniciar la estadía (registra observers y cambia estado si es necesario)
        prepararEstadiaParaInicio(estadia);
        estadia.iniciarEstadia();
        estadiaRepository.save(estadia);

        return mapper.toDTOEstadia(estadia);
    }
}
