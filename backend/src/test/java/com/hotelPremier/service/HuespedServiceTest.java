package com.hotelPremier.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hotelPremier.classes.DTO.HuespedDTO;
import com.hotelPremier.classes.Dominio.Direccion;
import com.hotelPremier.classes.Dominio.Huesped;
import com.hotelPremier.classes.Dominio.HuespedID;
import com.hotelPremier.exception.RecursoNoEncontradoException;
import com.hotelPremier.repository.DireccionRepository;
import com.hotelPremier.repository.HuespedRepository;
import com.hotelPremier.classes.mapper.ClassMapper;

@ExtendWith(MockitoExtension.class)
class HuespedServiceTest {

    @Mock
    private HuespedRepository huespedRepository;

    @Mock
    private DireccionRepository direccionRepository;

    @Mock
    private ClassMapper mapper;

    @InjectMocks
    private HuespedService huespedService;

    /**
     * Devuelve todos los huéspedes existentes convertidos a DTO.
     */
    @Test
    void findAll_devuelveListaHuespedes() {

        Huesped h1 = new Huesped();
        Huesped h2 = new Huesped();
        List<Huesped> huespedes = Arrays.asList(h1, h2);

        HuespedDTO d1 = new HuespedDTO();
        HuespedDTO d2 = new HuespedDTO();
        List<HuespedDTO> dtos = Arrays.asList(d1, d2);

        when(huespedRepository.findAll()).thenReturn(huespedes);
        when(mapper.toDtos(huespedes)).thenReturn(dtos);

        List<HuespedDTO> resultado = huespedService.findAll();

        assertEquals(2, resultado.size());
    }

    /**
     * Devuelve huéspedes filtrados por DNI.
     */
    @Test
    void findByCategory_devuelveHuespedesPorDni() {

        String dni = "12345678";

        Huesped huesped = new Huesped();
        List<Huesped> huespedes = List.of(huesped);

        when(huespedRepository.findByHuespedID_Dni(dni))
                .thenReturn(huespedes);
        when(mapper.toDtos(huespedes))
                .thenReturn(List.of(new HuespedDTO()));

        List<HuespedDTO> resultado =
                huespedService.findByCategory(dni);

        assertEquals(1, resultado.size());
    }

    /**
     * Busca huéspedes aplicando filtros combinados.
     */
    @Test
    void buscarHuespedes_devuelveHuespedesFiltrados() {

        String dni = "12345678";
        String nombre = "Juan";
        String apellido = "Perez";
        String tipoDocumento = "DNI";

        List<Huesped> huespedes = List.of(new Huesped());

        when(huespedRepository.buscarHuespedes(dni, nombre, apellido, tipoDocumento))
                .thenReturn(huespedes);
        when(mapper.toDtos(huespedes))
                .thenReturn(List.of(new HuespedDTO()));

        List<HuespedDTO> resultado =
                huespedService.buscarHuespedes(dni, nombre, apellido, tipoDocumento);

        assertEquals(1, resultado.size());
    }

    /**
     * Da de alta un huésped persistiendo su dirección asociada.
     */
    @Test
    void addHuesped_guardaHuespedYDireccion() {

        HuespedDTO dto = new HuespedDTO();

        Direccion direccion = new Direccion();
        Huesped huesped = new Huesped();
        huesped.setDireccion(direccion);

        Huesped guardado = new Huesped();
        guardado.setDireccion(direccion);

        when(mapper.toEntity(dto)).thenReturn(huesped);
        when(direccionRepository.save(direccion)).thenReturn(direccion);
        when(huespedRepository.save(huesped)).thenReturn(guardado);
        when(mapper.toDTO(guardado)).thenReturn(new HuespedDTO());

        HuespedDTO resultado = huespedService.addHuesped(dto);

        assertNotNull(resultado);
    }

    /**
     * Falla la actualización si el identificador del huésped es nulo.
     */
    @Test
    void updateHuesped_huespedIDNulo_falla() {

        HuespedDTO dto = new HuespedDTO();
        dto.setHuespedID(null);

        assertThrows(IllegalArgumentException.class,
                () -> huespedService.updateHuesped(dto));
    }

    /**
     * Falla la actualización si el huésped no existe en el sistema.
     */
    @Test
    void updateHuesped_huespedNoExiste_falla() {

        HuespedDTO dto = new HuespedDTO();
        com.hotelPremier.classes.DTO.HuespedIDDTO idDTO =
                new com.hotelPremier.classes.DTO.HuespedIDDTO();
        idDTO.setTipoDocumento("DNI");
        idDTO.setDni("12345678");
        dto.setHuespedID(idDTO);

        HuespedID id = new HuespedID();
        id.setTipoDocumento("DNI");
        id.setDni("12345678");

        when(huespedRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> huespedService.updateHuesped(dto));
    }

    /**
     * Actualiza correctamente los datos personales del huésped.
     */
    @Test
    void updateHuesped_exito() {

        HuespedDTO dto = new HuespedDTO();
        dto.setNombre("Juan");
        dto.setApellido("Perez");

        com.hotelPremier.classes.DTO.HuespedIDDTO idDTO =
                new com.hotelPremier.classes.DTO.HuespedIDDTO();
        idDTO.setTipoDocumento("DNI");
        idDTO.setDni("12345678");
        dto.setHuespedID(idDTO);

        HuespedID id = new HuespedID();
        id.setTipoDocumento("DNI");
        id.setDni("12345678");

        Direccion direccion = new Direccion();
        Huesped existente = new Huesped();
        existente.setHuespedID(id);
        existente.setDireccion(direccion);

        when(huespedRepository.findById(id))
                .thenReturn(Optional.of(existente));
        when(huespedRepository.save(existente))
                .thenReturn(existente);
        when(mapper.toDTO(existente))
                .thenReturn(new HuespedDTO());

        HuespedDTO resultado =
                huespedService.updateHuesped(dto);

        assertNotNull(resultado);
        assertEquals("Juan", existente.getNombre());
        assertEquals("Perez", existente.getApellido());
    }

    /**
     * Crea y asocia una nueva dirección si el huésped no tenía una previa.
     */
    @Test
    void updateHuesped_conNuevaDireccion_creaDireccion() {

        HuespedDTO dto = new HuespedDTO();
        com.hotelPremier.classes.DTO.HuespedIDDTO idDTO =
                new com.hotelPremier.classes.DTO.HuespedIDDTO();
        idDTO.setTipoDocumento("DNI");
        idDTO.setDni("12345678");
        dto.setHuespedID(idDTO);

        com.hotelPremier.classes.DTO.DireccionDTO dirDTO =
                new com.hotelPremier.classes.DTO.DireccionDTO();
        dirDTO.setCalle("Nueva Calle");
        dto.setDireccion(dirDTO);

        HuespedID id = new HuespedID();
        id.setTipoDocumento("DNI");
        id.setDni("12345678");

        Huesped existente = new Huesped();
        existente.setHuespedID(id);
        existente.setDireccion(null);

        when(huespedRepository.findById(id))
                .thenReturn(Optional.of(existente));
        when(direccionRepository.save(any(Direccion.class)))
                .thenReturn(new Direccion());
        when(huespedRepository.save(existente))
                .thenReturn(existente);
        when(mapper.toDTO(existente))
                .thenReturn(new HuespedDTO());

        huespedService.updateHuesped(dto);

        assertNotNull(existente.getDireccion());
    }

    /**
     * Falla la eliminación si el huésped no existe.
     */
    @Test
    void deleteHuesped_noExiste_falla() {

        when(huespedRepository.existsById(any()))
                .thenReturn(false);

        assertThrows(RecursoNoEncontradoException.class,
                () -> huespedService.deleteHuesped("DNI", "12345678"));
    }

    /**
     * Elimina correctamente un huésped existente.
     */
    @Test
    void deleteHuesped_exito() {

        when(huespedRepository.existsById(any()))
                .thenReturn(true);

        huespedService.deleteHuesped("DNI", "12345678");

        verify(huespedRepository).deleteById(any());
    }
}
