package com.hotelPremier.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hotelPremier.classes.DTO.HabitacionDTO;
import com.hotelPremier.classes.DTO.ReservaDTO;
import com.hotelPremier.classes.Dominio.Habitacion;
import com.hotelPremier.classes.Dominio.Reserva;
import com.hotelPremier.classes.mapper.ClassMapper;
import com.hotelPremier.repository.HabitacionRepository;
import com.hotelPremier.repository.ReservaRepository;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private HabitacionRepository habitacionRepository;

    @Mock
    private ClassMapper mapper;

    @InjectMocks
    private ReservaService reservaService;

    // ============================
    // Reserva válida
    // ============================
    @Test
    void reservaValida_seRegistraCorrectamente() {

        ReservaDTO dto = new ReservaDTO();
        dto.setApellido("PEREZ");
        dto.setNombre("JUAN");
        dto.setTelefono("3421234567");
        dto.setFecha_desde(new Date());
        dto.setFecha_hasta(new Date());

        HabitacionDTO habDTO = new HabitacionDTO();
        habDTO.setNumero(101);
        dto.setHabitacion(habDTO);

        Habitacion habitacion = new Habitacion();
        habitacion.setNumero(101);

        Reserva reserva = new Reserva();
        reserva.setEstado("PENDIENTE");

        when(mapper.toEntityReserva(any(ReservaDTO.class)))
                .thenReturn(reserva);

        when(habitacionRepository.findById(101)).thenReturn(Optional.of(habitacion));

        when(reservaRepository.haySuperposicion(
                eq(101), any(), any())
        ).thenReturn(0);

        reservaService.guardarLista(List.of(dto));

        verify(reservaRepository, times(1))
                .saveAll(any());
    }

    // ============================
    // Habitación inexistente
    // ============================
    @Test
    void fallaSiNoExisteHabitacion() {

        ReservaDTO dto = new ReservaDTO();
        HabitacionDTO habDTO = new HabitacionDTO();
        habDTO.setNumero(101);
        dto.setHabitacion(habDTO);

        assertThrows(RuntimeException.class,
                () -> reservaService.guardarLista(List.of(dto)));

        verify(reservaRepository, never())
                .saveAll(any());
    }

    // ============================
    // Faltan datos del titular
    // ============================
    @Test
    void fallaSiFaltanDatosDelTitular() {

        ReservaDTO dto = new ReservaDTO();
        dto.setApellido("PEREZ");

        assertThrows(RuntimeException.class,
                () -> reservaService.guardarLista(List.of(dto)));

        verify(reservaRepository, never())
                .saveAll(any());
    }

    // ============================
    // Fechas solapadas
    // ============================
    @Test
    void fallaSiHayFechasSolapadas() {

        ReservaDTO dto = new ReservaDTO();
        dto.setApellido("PEREZ");
        dto.setNombre("JUAN");
        dto.setTelefono("3421234567");
        dto.setFecha_desde(new Date());
        dto.setFecha_hasta(new Date());

        HabitacionDTO habDTO = new HabitacionDTO();
        habDTO.setNumero(101);
        dto.setHabitacion(habDTO);

        Habitacion habitacion = new Habitacion();
        habitacion.setNumero(101);

        when(habitacionRepository.findById(101))
                .thenReturn(Optional.of(habitacion));

        when(reservaRepository.haySuperposicion(
                eq(101), any(), any())
        ).thenReturn(1);

        assertThrows(RuntimeException.class,
                () -> reservaService.guardarLista(List.of(dto)));

        verify(reservaRepository, never())
                .saveAll(any());
    }

    // ============================
    // Cancelar reserva pendiente
    // ============================
    @Test
    void cancelarReservaPendiente_exito() {

        Reserva reserva = new Reserva();
        reserva.setId_reserva(1);
        reserva.setEstado("PENDIENTE");

        when(reservaRepository.findById(1))
                .thenReturn(Optional.of(reserva));

        when(mapper.toDTOReserva(any(Reserva.class)))
                .thenReturn(new ReservaDTO());

        reservaService.cancelarReserva(1);

        verify(reservaRepository, times(1)).save(reserva);
        assertEquals("CANCELADA", reserva.getEstado());
    }

    // ============================
    // Cancelar ya cancelada
    // ============================
    @Test
    void cancelarReservaYaCancelada_falla() {

        Reserva reserva = new Reserva();
        reserva.setId_reserva(2);
        reserva.setEstado("CANCELADA");

        when(reservaRepository.findById(2))
                .thenReturn(Optional.of(reserva));

        assertThrows(IllegalStateException.class,
                () -> reservaService.cancelarReserva(2));

        verify(reservaRepository, never()).save(any());
    }

    // ============================
    // Cancelar inexistente
    // ============================
    @Test
    void cancelarReservaInexistente_falla() {

        when(reservaRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> reservaService.cancelarReserva(99));

        verify(reservaRepository, never()).save(any());
    }
}
