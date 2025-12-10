package com.hotelPremier.service;

import com.hotelPremier.classes.DTO.ReservaDTO;
import com.hotelPremier.classes.Dominio.Habitacion;
import com.hotelPremier.classes.Dominio.Reserva;
import com.hotelPremier.classes.mapper.ClassMapper;
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
}
