package com.hotelPremier.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hotelPremier.classes.DTO.PagoDTO;
import com.hotelPremier.classes.DTO.medioDePago.MonedaLocalDTO;
import com.hotelPremier.classes.Dominio.Factura;
import com.hotelPremier.repository.FacturaRepository;
import com.hotelPremier.repository.PagoRepository;

@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private FacturaRepository facturaRepository;

    @InjectMocks
    private PagoService pagoService;

    /**
     * Verifica que falle el registro de pago
     * cuando el DTO recibido es nulo.
     */
    @Test
    void ingresarPago_dtoNulo_falla() {

        assertThrows(IllegalArgumentException.class,
                () -> pagoService.ingresarPago(null));
    }

    /**
     * Verifica que falle el registro de pago
     * cuando la factura no existe.
     */
    @Test
    void ingresarPago_facturaInexistente_falla() {

        PagoDTO dto = new PagoDTO();
        dto.setFactura(new com.hotelPremier.classes.DTO.FacturaDTO());
        dto.getFactura().setID(99);

        when(facturaRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> pagoService.ingresarPago(dto));
    }

    /**
     * Verifica que falle el registro de pago
     * cuando no se envían medios de pago.
     */
    @Test
    void ingresarPago_sinMedios_falla() {

        PagoDTO dto = new PagoDTO();
        dto.setFactura(new com.hotelPremier.classes.DTO.FacturaDTO());
        dto.getFactura().setID(1);
        dto.setMedios(List.of());

        Factura factura = new Factura();

        when(facturaRepository.findById(1))
                .thenReturn(Optional.of(factura));

        assertThrows(IllegalArgumentException.class,
                () -> pagoService.ingresarPago(dto));
    }

    /**
     * Verifica que falle el registro de pago
     * cuando la suma de los medios no coincide
     * con el monto total del pago.
     */
    @Test
    void ingresarPago_sumaNoCoincide_falla() {

        PagoDTO dto = new PagoDTO();
        dto.setMonto(1000);

        dto.setFactura(new com.hotelPremier.classes.DTO.FacturaDTO());
        dto.getFactura().setID(1);

        MonedaLocalDTO medio = new MonedaLocalDTO();
        medio.setTipo("MONEDA_LOCAL");
        medio.setMonto(500);
        medio.setFecha(new Date());
        medio.setTipoMoneda("ARS");

        dto.setMedios(List.of(medio));

        Factura factura = new Factura();

        when(facturaRepository.findById(1))
                .thenReturn(Optional.of(factura));

        assertThrows(IllegalArgumentException.class,
                () -> pagoService.ingresarPago(dto));
    }

    /**
     * Verifica el registro exitoso de un pago
     * cuando los datos son válidos.
     */
    @Test
    void ingresarPago_exito() {

        PagoDTO dto = new PagoDTO();
        dto.setMonto(500);

        dto.setFactura(new com.hotelPremier.classes.DTO.FacturaDTO());
        dto.getFactura().setID(1);

        MonedaLocalDTO medio = new MonedaLocalDTO();
        medio.setTipo("MONEDA_LOCAL");
        medio.setMonto(500);
        medio.setFecha(new Date());
        medio.setTipoMoneda("ARS");

        dto.setMedios(List.of(medio));

        Factura factura = new Factura();
        factura.setEstado("PENDIENTE");

        when(facturaRepository.findById(1))
                .thenReturn(Optional.of(factura));

        String resultado = pagoService.ingresarPago(dto);

        assertEquals("Pago registrado correctamente.", resultado);
        verify(pagoRepository, times(1)).save(any());
        verify(facturaRepository, times(1)).save(factura);
    }
}
