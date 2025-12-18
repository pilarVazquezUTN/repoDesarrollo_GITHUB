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

import com.hotelPremier.classes.DTO.EstadiaDTO;
import com.hotelPremier.classes.DTO.FacturaDTO;
import com.hotelPremier.classes.Dominio.*;
import com.hotelPremier.classes.mapper.ClassMapper;
import com.hotelPremier.classes.exception.RecursoNoEncontradoException;
import com.hotelPremier.repository.*;

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
     * Devuelve solo las facturas asociadas a estadías finalizadas de una habitación.
     */
    @Test
    void obtenerFacturasPorHabitacion_devuelveSoloFacturasFinalizadas() {

        Habitacion hab = new Habitacion();
        hab.setNumero(101);

        Estadia enCurso = new Estadia();
        enCurso.setEstado("ENCURSO");
        enCurso.setHabitacion(hab);

        Factura f1 = new Factura();
        f1.setEstadia(enCurso);

        Estadia finalizada = new Estadia();
        finalizada.setEstado("FINALIZADA");
        finalizada.setHabitacion(hab);

        Factura f2 = new Factura();
        f2.setEstadia(finalizada);

        when(facturaRepository.findAll())
                .thenReturn(List.of(f1, f2));
        when(mapper.toDTOFactura(f2))
                .thenReturn(new FacturaDTO());

        List<FacturaDTO> resultado =
                facturaService.obtenerFacturasPorHabitacion(101);

        assertEquals(1, resultado.size());
    }

    /**
     * Crea una factura correctamente cuando la estadía existe y está completa.
     */
    @Test
    void crearFactura_exito() {

        Habitacion hab = new Habitacion();
        hab.setNumero(101);
        hab.setPrecio(100);

        Estadia estadia = new Estadia();
        estadia.setId_estadia(1);
        estadia.setHabitacion(hab);
        estadia.setCheckin(new java.util.Date(System.currentTimeMillis() - 86400000));
        estadia.setCheckout(new java.util.Date());

        EstadiaDTO estadiaDTO = new EstadiaDTO();
        estadiaDTO.setID(1);

        FacturaDTO dto = new FacturaDTO();
        dto.setEstadia(estadiaDTO);

        when(estadiaRepository.findById(1))
                .thenReturn(Optional.of(estadia));
        when(mapper.toEntityFactura(any()))
                .thenReturn(new Factura());
        when(mapper.toDTOFactura(any()))
                .thenReturn(new FacturaDTO());

        FacturaDTO resultado = facturaService.crearFactura(dto);

        assertNotNull(resultado);
        verify(facturaRepository).save(any());
    }

    /**
     * Falla la creación de factura si la estadía no existe.
     */
    @Test
    void crearFactura_fallaSiEstadiaNoExiste() {

        EstadiaDTO estadiaDTO = new EstadiaDTO();
        estadiaDTO.setID(99);

        FacturaDTO dto = new FacturaDTO();
        dto.setEstadia(estadiaDTO);

        when(estadiaRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> facturaService.crearFactura(dto));
    }

    /**
     * Filtra facturas por CUIT del responsable de pago.
     */
    @Test
    void filtrarFacturas_porCuit_devuelveFacturas() {

        when(facturaRepository.findAll())
                .thenReturn(List.of(new Factura()));

        List<FacturaDTO> resultado =
                facturaService.filtrarFacturas("20-12345678-9", null, null);

        assertNotNull(resultado);
    }

    /**
     * Filtra facturas por tipo y número de documento.
     */
    @Test
    void filtrarFacturas_porTipoYNumeroDoc_devuelveFacturas() {

        when(facturaRepository.findAll())
                .thenReturn(List.of(new Factura()));

        List<FacturaDTO> resultado =
                facturaService.filtrarFacturas(null, "DNI", "12345678");

        assertNotNull(resultado);
    }

    /**
     * Devuelve todas las facturas cuando no se aplican filtros.
     */
    @Test
    void filtrarFacturas_sinFiltros_devuelveTodas() {

        when(facturaRepository.findAll())
                .thenReturn(List.of(new Factura(), new Factura()));

        when(mapper.toDTOFactura(any()))
                .thenReturn(new FacturaDTO());

        List<FacturaDTO> resultado =
                facturaService.filtrarFacturas(null, null, null);

        assertEquals(2, resultado.size());
    }

    /**
     * Falla la generación de factura final si la factura no existe.
     */
    @Test
    void generarFacturaFinal_facturaInexistente_falla() {

        when(facturaRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> facturaService.generarFacturaFinal(99));
    }

    /**
     * Genera la factura final y finaliza la estadía.
     */
    @Test
    void generarFacturaFinal_exito() {

        Habitacion hab = new Habitacion();
        hab.setNumero(101);

        Estadia estadia = new Estadia();
        estadia.setHabitacion(hab);

        Factura factura = new Factura();
        factura.setEstadia(estadia);

        when(facturaRepository.findById(1))
                .thenReturn(Optional.of(factura));
        when(mapper.toDTOFactura(any()))
                .thenReturn(new FacturaDTO());

        FacturaDTO resultado =
                facturaService.generarFacturaFinal(1);

        assertNotNull(resultado);
    }

    /**
     * Falla el pago si la factura no existe.
     */
    @Test
    void pagarFactura_facturaInexistente_falla() {

        when(facturaRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> facturaService.pagarFactura(99));
    }

    /**
     * Marca una factura como pagada cuando tiene un pago asociado.
     */
    @Test
    void pagarFactura_exito() {

        Factura factura = new Factura();
        Pago pago = new Pago();
        pago.setMonto(1000);
        factura.setPago(pago);

        when(facturaRepository.findById(1))
                .thenReturn(Optional.of(factura));
        when(mapper.toDTOFactura(any()))
                .thenReturn(new FacturaDTO());

        FacturaDTO resultado =
                facturaService.pagarFactura(1);

        assertNotNull(resultado);
    }

    /**
     * Falla la aplicación de nota de crédito si la factura no existe.
     */
    @Test
    void aplicarNotaCreditoAFactura_facturaInexistente_falla() {

        when(facturaRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> facturaService.aplicarNotaCreditoAFactura(99));
    }

    /**
     * Cancela una factura cuando tiene nota de crédito asociada.
     */
    @Test
    void aplicarNotaCreditoAFactura_exito() {

        Factura factura = new Factura();
        NotaDeCredito nc = new NotaDeCredito();
        nc.setImporte(1000);
        factura.setNotaDeCredito(nc);

        when(facturaRepository.findById(1))
                .thenReturn(Optional.of(factura));
        when(mapper.toDTOFactura(any()))
                .thenReturn(new FacturaDTO());

        FacturaDTO resultado =
                facturaService.aplicarNotaCreditoAFactura(1);

        assertNotNull(resultado);
    }
}
