package com.hotelPremier.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hotelPremier.classes.Dominio.HuespedID;
import com.hotelPremier.repository.DireccionRepository;
import com.hotelPremier.repository.HuespedRepository;

@ExtendWith(MockitoExtension.class)
class HuespedServiceTest {

    @Mock
    private HuespedRepository huespedRepository;

    @Mock
    private DireccionRepository direccionRepository;

    @Mock
    private com.hotelPremier.classes.mapper.ClassMapper mapper;

    @InjectMocks
    private HuespedService huespedService;

    /**
     * Test para cuando el huesped no existe en la base de datos, falla y lanza una excepcion
     */
    @Test
    void deleteHuesped_noExiste_falla() {

        when(huespedRepository.existsById(any()))
                .thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> huespedService.deleteHuesped("DNI", "12345678"));

        verify(huespedRepository, never())
                .deleteById(any());
    }

    /**
     * Test para caso de exito al eliminar un huesped (existe en la base de datos).
     */
    @Test
    void deleteHuesped_exito() {

        when(huespedRepository.existsById(any()))
                .thenReturn(true);

        huespedService.deleteHuesped("DNI", "12345678");

        verify(huespedRepository, times(1))
                .deleteById(any());
    }
}
