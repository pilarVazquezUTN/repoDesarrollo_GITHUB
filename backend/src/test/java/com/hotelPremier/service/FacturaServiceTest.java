package com.hotelPremier.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hotelPremier.classes.DTO.FacturaDTO;
import com.hotelPremier.classes.Dominio.Estadia;
import com.hotelPremier.classes.Dominio.Factura;
import com.hotelPremier.classes.Dominio.Habitacion;
import com.hotelPremier.classes.mapper.ClassMapper;
import com.hotelPremier.repository.EstadiaRepository;
import com.hotelPremier.repository.FacturaRepository;
import com.hotelPremier.repository.HabitacionRepository;
import com.hotelPremier.repository.ResponsablePagoRepository;
import com.hotelPremier.repository.ServicioExtraRepository;

@ExtendWith(MockitoExtension.class)
class FacturaServiceTest {

    @Mock
    private FacturaRepository facturaRepository;

    @Mock
    private EstadiaRepository estadiaRepository;

    @Mock
    private ResponsablePagoRepository responsablePagoRepository;

    @Mock
    private ServicioExtraRepository servicioExtraRepository;

    @Mock
    private HabitacionRepository habitacionRepository;

    @Mock
    private ClassMapper mapper;

    @InjectMocks
    private FacturaService facturaService;

    /**
     * Verifica que se devuelvan únicamente facturas
     * asociadas a estadías en curso para una habitación.
     */
    @Test
    void obtenerFacturasPorHabitacion_devuelveSoloFacturasEnCurso() {

        Habitacion hab = new Habitacion();
        hab.setNumero(101);

        Estadia estadiaEnCurso = new Estadia();
        estadiaEnCurso.setEstado("ENCURSO");
        estadiaEnCurso.setHabitacion(hab);

        Factura facturaEnCurso = new Factura();
        facturaEnCurso.setEstadia(estadiaEnCurso);

        Estadia estadiaFinalizada = new Estadia();
        estadiaFinalizada.setEstado("FINALIZADA");
        estadiaFinalizada.setHabitacion(hab);

        Factura facturaFinalizada = new Factura();
        facturaFinalizada.setEstadia(estadiaFinalizada);

        when(facturaRepository.findAll())
                .thenReturn(List.of(facturaEnCurso, facturaFinalizada));

        when(mapper.toDTOFactura(any(Factura.class)))
                .thenReturn(new FacturaDTO());

        List<FacturaDTO> resultado =
                facturaService.obtenerFacturasPorHabitacion(101);

        assertEquals(1, resultado.size());
    }

    /**
     * Verifica la creación correcta de una factura
     * cuando la estadía existe.
     */
    @Test
    void crearFactura_exito() {

        FacturaDTO dto = new FacturaDTO();
        dto.setTotal(500);

        Estadia estadia = new Estadia();
        estadia.setId_estadia(1);

        dto.setEstadia(new com.hotelPremier.classes.DTO.EstadiaDTO());
        dto.getEstadia().setID(1);

        Factura factura = new Factura();

        when(mapper.toEntityFactura(any(FacturaDTO.class)))
                .thenReturn(factura);

        when(estadiaRepository.findById(1))
                .thenReturn(Optional.of(estadia));

        when(mapper.toDTOFactura(any(Factura.class)))
                .thenReturn(new FacturaDTO());

        FacturaDTO resultado = facturaService.crearFactura(dto);

        assertNotNull(resultado);
        verify(facturaRepository, times(1)).save(factura);
    }

    /**
     * Verifica que falle la creación de la factura
     * cuando la estadía no existe.
     */
    @Test
    void crearFactura_fallaSiEstadiaNoExiste() {

        FacturaDTO dto = new FacturaDTO();
        dto.setEstadia(new com.hotelPremier.classes.DTO.EstadiaDTO());
        dto.getEstadia().setID(99);

        when(mapper.toEntityFactura(any()))
                .thenReturn(new Factura());

        when(estadiaRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> facturaService.crearFactura(dto));
    }
}
