package com.hotelPremier.classes.reserva;

import org.springframework.stereotype.Service;

import com.hotelPremier.repository.ReservaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GestorReservaService implements GestorReservaInterfaz{
String id_empleado;

    // Inyección de Dependencia
    private final ReservaRepository reservaRepository;

    // 2. Spring inyecta la implementación del Repositorio JPA aquí
    public GestorReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }


    public void setIdEmpleado(String idEmpleado) {
    id_empleado = idEmpleado;
    }

    /**
     * Devuelve el id del empleado asociado.
     *
     * @return id del empleado
     */
    public String getIdEmpleado() {
        return  id_empleado;
    }

    /**
     * Registra una nueva reserva.
     */
    public void reservarHabitacion() {

    }

    /**
     * Cancela una reserva existente.
     */
    public void cancelarReserva() {

    }

    /**
     * Marca el ingreso de una reserva al sistema.
     */
    public void ingresaReserva() {

    }

    /**
     * Realiza el proceso de check-out de una reserva.
     */
    public void realizarCheckOut() {

    }
    public void realizarCheckIn() {

    }


    /**
     * busca reservas con el apellido pasado por parametro
     * @param apellido
     * @return
     */
    public List<ReservaDTO> reservas(String apellido) {
        // 1. EL SERVICIO DECIDE QUÉ MÉTODO DEL REPOSITORIO USAR
        List<Reserva> reservasEntidad;

        if (apellido == null || apellido.trim().isEmpty()) {
            // Llama al findAll() provisto por JpaRepository
            reservasEntidad = reservaRepository.findAll();
        } else {
            // Llama al método derivado del Repositorio
            reservasEntidad = reservaRepository.findByApellidoContainingIgnoreCase(apellido);
        }

        // 2. EL SERVICIO ES RESPONSABLE DE CONVERTIR (Mapeo)
        // Se convierte la Entidad JPA (Reserva) al formato de transferencia (ReservaDTO)
        return mapToDTOList(reservasEntidad);
    }
    private List<ReservaDTO> mapToDTOList(List<Reserva> entidades) {

        if (entidades == null || entidades.isEmpty()) {
            return new ArrayList<>();
        }

        // Usa Java Streams para un mapeo conciso y funcional:
        return entidades.stream()
                // Por cada 'entidad' en la lista, llama a 'mapToDTO'
                .map(this::mapToDTO)
                .collect(Collectors.toList()); // Recolecta en la lista final
    }


     /** Mapea una única Entidad Reserva a un único ReservaDTO.
     */
    private ReservaDTO mapToDTO(Reserva entidad) {
        ReservaDTO dto = new ReservaDTO();

        //  Copia de datos (de Entidad a DTO)
        // ¡Debes mapear todos los campos que necesite tu DTO!

        dto.setNumeroHab(entidad.getNro_habitacion());
        dto.setNombre(entidad.getNombre());
        dto.setApellido(entidad.getApellido());
        //dto.setTipoHab(entidad.getTipohab); """"""ESTOOOOOO NOSEEEEEEEEE PROBARRRRRR!!!!!"""""""""""
        dto.setFechaDesde(entidad.getFecha_desde());
        dto.setFechaHasta(entidad.getFecha_hasta());



        return dto;
    }

    }












