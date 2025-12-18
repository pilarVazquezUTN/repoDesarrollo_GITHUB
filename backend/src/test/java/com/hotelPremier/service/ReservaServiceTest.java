package com.hotelPremier.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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
import com.hotelPremier.classes.Dominio.Estadia;
import com.hotelPremier.classes.mapper.ClassMapper;
import com.hotelPremier.exception.NegocioException;
import com.hotelPremier.exception.RecursoNoEncontradoException;
import com.hotelPremier.repository.HabitacionRepository;
import com.hotelPremier.repository.ReservaRepository;
import com.hotelPremier.repository.EstadiaRepository;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private HabitacionRepository habitacionRepository;

    @Mock
    private EstadiaRepository estadiaRepository;

    @Mock
    private EstadiaService estadiaService;

    @Mock
    private ClassMapper mapper;

    @InjectMocks
    private ReservaService reservaService;

    /**
     * Registra correctamente una reserva válida sin superposición de fechas.
     */
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

        when(mapper.toEntityReserva(any()))
                .thenReturn(reserva);
        when(habitacionRepository.findById(101))
                .thenReturn(Optional.of(habitacion));
        when(reservaRepository.haySuperposicion(eq(101), any(), any()))
                .thenReturn(0);

        reservaService.guardarLista(List.of(dto));

        verify(reservaRepository).saveAll(any());
    }

    /**
     * Falla el registro si la habitación no existe.
     */
    @Test
    void fallaSiNoExisteHabitacion() {

        ReservaDTO dto = new ReservaDTO();
        HabitacionDTO habDTO = new HabitacionDTO();
        habDTO.setNumero(101);
        dto.setHabitacion(habDTO);

        assertThrows(RecursoNoEncontradoException.class,
                () -> reservaService.guardarLista(List.of(dto)));

        verify(reservaRepository, never()).saveAll(any());
    }

    /**
     * Falla el registro si faltan datos obligatorios del titular.
     */
    @Test
    void fallaSiFaltanDatosDelTitular() {

        ReservaDTO dto = new ReservaDTO();
        dto.setApellido("PEREZ");

        assertThrows(IllegalArgumentException.class,
                () -> reservaService.guardarLista(List.of(dto)));

        verify(reservaRepository, never()).saveAll(any());
    }

    /**
     * Falla el registro cuando existen fechas solapadas.
     */
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
        when(reservaRepository.haySuperposicion(eq(101), any(), any()))
                .thenReturn(1);

        assertThrows(NegocioException.class,
                () -> reservaService.guardarLista(List.of(dto)));

        verify(reservaRepository, never()).saveAll(any());
    }

    /**
     * Cancela correctamente una reserva pendiente.
     */
    @Test
    void cancelarReservaPendiente_exito() {

        Reserva reserva = new Reserva();
        reserva.setId_reserva(1);
        reserva.setEstado("PENDIENTE");

        when(reservaRepository.findById(1))
                .thenReturn(Optional.of(reserva));
        when(mapper.toDTOReserva(any()))
                .thenReturn(new ReservaDTO());

        reservaService.cancelarReserva(1);

        assertEquals("CANCELADA", reserva.getEstado());
        verify(reservaRepository).save(reserva);
    }

    /**
     * Falla la cancelación si la reserva ya está cancelada.
     */
    @Test
    void cancelarReservaYaCancelada_falla() {

        Reserva reserva = new Reserva();
        reserva.setId_reserva(2);
        reserva.setEstado("CANCELADA");

        when(reservaRepository.findById(2))
                .thenReturn(Optional.of(reserva));

        assertThrows(NegocioException.class,
                () -> reservaService.cancelarReserva(2));

        verify(reservaRepository, never()).save(any());
    }

    /**
     * Falla la cancelación si la reserva no existe.
     */
    @Test
    void cancelarReservaInexistente_falla() {

        when(reservaRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> reservaService.cancelarReserva(99));

        verify(reservaRepository, never()).save(any());
    }

    /**
     * Devuelve reservas dentro de un rango de fechas.
     */
    @Test
    void buscarEntreFechas_devuelveReservasEnRango() {

        java.time.LocalDate desde = java.time.LocalDate.of(2024, 1, 1);
        java.time.LocalDate hasta = java.time.LocalDate.of(2024, 1, 31);

        List<Reserva> reservas = List.of(new Reserva(), new Reserva());
        List<ReservaDTO> dtos = List.of(new ReservaDTO(), new ReservaDTO());

        when(reservaRepository.buscarEntreFechas(desde, hasta))
                .thenReturn(reservas);
        when(mapper.toDtosReserva(reservas))
                .thenReturn(dtos);

        List<ReservaDTO> resultado =
                reservaService.buscarEntreFechas(desde, hasta);

        assertEquals(2, resultado.size());
    }

    /**
     * Busca reservas por apellido y nombre.
     */
    @Test
    void buscarPorApellidoNombre_conApellidoYNombre_devuelveReservas() {

        List<Reserva> reservas = List.of(new Reserva());

        when(reservaRepository
                .findByApellidoStartingWithIgnoreCaseAndNombreStartingWithIgnoreCase("Perez", "Juan"))
                .thenReturn(reservas);
        when(mapper.toDtosReserva(reservas))
                .thenReturn(List.of(new ReservaDTO()));

        List<ReservaDTO> resultado =
                reservaService.buscarPorApellidoNombre("Perez", "Juan");

        assertEquals(1, resultado.size());
    }

    /**
     * Busca reservas solo por apellido.
     */
    @Test
    void buscarPorApellidoNombre_soloApellido_devuelveReservas() {

        List<Reserva> reservas = List.of(new Reserva());

        when(reservaRepository.findByApellidoStartingWithIgnoreCase("Perez"))
                .thenReturn(reservas);
        when(mapper.toDtosReserva(reservas))
                .thenReturn(List.of(new ReservaDTO()));

        List<ReservaDTO> resultado =
                reservaService.buscarPorApellidoNombre("Perez", null);

        assertEquals(1, resultado.size());
    }

    /**
     * Busca reservas solo por nombre.
     */
    @Test
    void buscarPorApellidoNombre_soloNombre_devuelveReservas() {

        List<Reserva> reservas = List.of(new Reserva());

        when(reservaRepository.findByNombreStartingWithIgnoreCase("Juan"))
                .thenReturn(reservas);
        when(mapper.toDtosReserva(reservas))
                .thenReturn(List.of(new ReservaDTO()));

        List<ReservaDTO> resultado =
                reservaService.buscarPorApellidoNombre(null, "Juan");

        assertEquals(1, resultado.size());
    }

    /**
     * Devuelve todas las reservas si no hay filtros.
     */
    @Test
    void buscarPorApellidoNombre_sinParametros_devuelveTodas() {

        List<Reserva> reservas = List.of(new Reserva(), new Reserva());

        when(reservaRepository.findAll())
                .thenReturn(reservas);
        when(mapper.toDtosReserva(reservas))
                .thenReturn(List.of(new ReservaDTO(), new ReservaDTO()));

        List<ReservaDTO> resultado =
                reservaService.buscarPorApellidoNombre(null, null);

        assertEquals(2, resultado.size());
    }

    /**
     * Falla el check-in si la reserva no existe.
     */
    @Test
    void hacerCheckIn_reservaInexistente_falla() {

        when(reservaRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> reservaService.hacerCheckIn(99));

        verify(reservaRepository, never()).save(any());
    }

    /**
     * Realiza el check-in correctamente y genera la estadía.
     */
    @Test
    void hacerCheckIn_exito() {

        Reserva reserva = new Reserva();
        reserva.setId_reserva(1);
        reserva.setEstado("PENDIENTE");

        Habitacion habitacion = new Habitacion();
        habitacion.setNumero(101);

        Estadia estadia = new Estadia();
        estadia.setHabitacion(habitacion);

        Reserva reservaSpy = spy(reserva);

        when(reservaRepository.findById(1))
                .thenReturn(Optional.of(reservaSpy));
        doReturn(estadia).when(reservaSpy).checkIn();
        when(estadiaService.iniciarEstadia(any()))
                .thenReturn(estadia);
        when(estadiaRepository.save(any()))
                .thenReturn(estadia);
        when(habitacionRepository.save(any()))
                .thenReturn(habitacion);
        when(reservaRepository.save(reservaSpy))
                .thenReturn(reservaSpy);

        Estadia resultado = reservaService.hacerCheckIn(1);

        assertNotNull(resultado);
        verify(reservaRepository).save(reservaSpy);
        verify(estadiaRepository).save(estadia);
        verify(habitacionRepository).save(habitacion);
    }
}
