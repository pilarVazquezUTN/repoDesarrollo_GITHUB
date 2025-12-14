package com.hotelPremier.service;

import com.hotelPremier.classes.DTO.ReservaDTO;
import com.hotelPremier.classes.Dominio.Estadia;
import com.hotelPremier.classes.Dominio.Habitacion;
import com.hotelPremier.classes.Dominio.Reserva;
import com.hotelPremier.classes.mapper.ClassMapper;
import com.hotelPremier.repository.EstadiaRepository;
import com.hotelPremier.repository.HabitacionRepository;
import com.hotelPremier.repository.ReservaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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


    // ============================================
    // 1) GUARDAR LISTA DE RESERVAS
    // ============================================
    public List<ReservaDTO> guardarLista(List<ReservaDTO> listaDTO) {

        List<Reserva> entidades = new ArrayList<>();

        for (ReservaDTO dto : listaDTO) {

            // validación base
            if (dto.getHabitacion() == null)
                throw new RuntimeException("El DTO de reserva no contiene la habitación.");

            Integer nro = dto.getHabitacion().getNumero();
            if (nro == null)
                throw new RuntimeException("El DTO no trae número de habitación.");

            // obtener habitación desde BD
            Habitacion hab = habitacionRepository.findById(nro)
                    .orElseThrow(() -> new RuntimeException("Habitación inexistente: " + nro));

            // validar superposición
            Integer solapa = reservaRepository.haySuperposicion(
                    nro,
                    dto.getFecha_desde(),
                    dto.getFecha_hasta()
            );

            if (solapa != null && solapa > 0)
                throw new RuntimeException("La habitación " + nro + " ya tiene reservas en ese rango.");

            // convertir DTO → Entity (sin habitacion)
            Reserva r = mapper.toEntityReserva(dto);
            if (r.getEstado() == null || r.getEstado().trim().isEmpty()) {
                r.setEstado("PENDIENTE");
            }
            // agregar habitación real
            r.setHabitacion(hab);

            entidades.add(r);
        }

        reservaRepository.saveAll(entidades);
        return mapper.toDtosReserva(entidades);
    }

    // ============================================
    // 2) BUSCAR ENTRE FECHAS
    // ============================================
    public List<ReservaDTO> buscarEntreFechas(Date desde, Date hasta) {
        List<Reserva> reservas = reservaRepository.buscarEntreFechas(desde, hasta);
        return mapper.toDtosReserva(reservas);
    }

    // ============================================
    // 3) BUSCAR POR APELLIDO + NOMBRE
    // ============================================
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

    // ============================================
    // 4) ELIMINAR RESERVA
    // ============================================
    public boolean deleteReserva(Integer id) {
        if (!reservaRepository.existsById(id)) {
            return false;
        }

        reservaRepository.deleteById(id);
        return true;
    }

    // ============================================
    // 5) HACER CHECK-IN DE UNA RESERVA
    // ============================================
    /**
     * Hace check-in de una reserva usando los patrones State y Observer.
     * 
     * La lógica de negocio está en el dominio (Reserva.checkIn()):
     * - Valida que la reserva esté en estado PENDIENTE (patrón State)
     * - Cambia la reserva a estado CONSUMIDA
     * - Crea una nueva Estadia en estado ENCURSO
     * - Asocia la estadía con la reserva
     * 
     * Luego usa el patrón Observer para:
     * - Actualizar la habitación a OCUPADA
     * - Confirmar que la reserva esté en CONSUMIDA
     * 
     * @param idReserva ID de la reserva a consumir
     * @return La nueva Estadia creada
     * @throws IllegalArgumentException si la reserva no existe o no está en estado PENDIENTE
     */
    public Estadia hacerCheckIn(Integer idReserva) {
        // 1. Buscar la reserva
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada con ID: " + idReserva));

        // 2. Usar el patrón State: el dominio valida y ejecuta la lógica
        // Si la reserva no está en PENDIENTE, lanzará IllegalStateException
        Estadia estadia = reserva.checkIn();

        // 3. Usar el patrón Observer: iniciar la estadía notificando a los observers
        // Esto actualiza la habitación a OCUPADA y confirma la reserva en CONSUMIDA
        estadia = estadiaService.iniciarEstadia(estadia);

        // 4. Persistir los cambios
        // La reserva cambió a CONSUMIDA, la habitación a OCUPADA, y la estadía está iniciada
        reservaRepository.save(reserva);
        estadiaRepository.save(estadia);
        
        // Persistir la habitación si fue actualizada por el observer
        if (estadia.getHabitacion() != null) {
            habitacionRepository.save(estadia.getHabitacion());
        }

        return estadia;
    }
}
