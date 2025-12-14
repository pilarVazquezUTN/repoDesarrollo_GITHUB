package com.hotelPremier.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotelPremier.classes.DTO.PagoDTO;
import com.hotelPremier.classes.DTO.medioDePago.*;

import com.hotelPremier.classes.Dominio.Pago;
import com.hotelPremier.classes.Dominio.Factura;

import com.hotelPremier.classes.Dominio.medioDePago.*;
import com.hotelPremier.classes.Dominio.medioDePago.strategy.MedioPagoStrategy;
import com.hotelPremier.classes.Dominio.medioDePago.strategy.SelectorMedioPagoStrategy;
import com.hotelPremier.classes.Dominio.factura.observer.PagoFacturaObserver;

import com.hotelPremier.repository.FacturaRepository;
import com.hotelPremier.repository.PagoRepository;

import java.math.BigDecimal;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private FacturaRepository facturaRepository;


    /**
     * Registra un pago usando el patrón Strategy para validar y calcular importes.
     * 
     * 1) Obtiene la Factura real desde la BD
     * 2) Selecciona la Strategy según el medio de pago
     * 3) Valida los datos del pago usando la Strategy
     * 4) Calcula el importe final del pago
     * 5) Registra el Pago asociado a la Factura
     */
    public String ingresarPago(PagoDTO dto) {

        if (dto == null) throw new IllegalArgumentException("JSON vacío.");
        if (dto.getFactura() == null || dto.getFactura().getID() == null)
            throw new IllegalArgumentException("Factura inválida.");

        // 1) Obtener la Factura real desde la BD
        Factura factura = facturaRepository.findById(dto.getFactura().getID())
            .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada."));

        if (dto.getMedios() == null || dto.getMedios().isEmpty())
            throw new IllegalArgumentException("Debe enviar al menos un medio de pago.");

        // Convertir DTOs a entidades y procesar con Strategy
        List<MedioDePago> listaMedios = new ArrayList<>();
        BigDecimal sumaCalculada = BigDecimal.ZERO;

        // Crear el Pago primero para poder pasarlo a las validaciones
        Pago pago = new Pago();
        pago.setFecha(new Date());
        pago.setFactura(factura);

        for (MedioDePagoDTO mpDTO : dto.getMedios()) {
            // Convertir DTO a entidad
            MedioDePago medioPago = convertirDTO(mpDTO);
            
            // 2) Seleccionar la Strategy según el medio de pago
            MedioPagoStrategy estrategia = SelectorMedioPagoStrategy.seleccionarEstrategia(medioPago);
            
            // 3) Validar los datos del pago usando la Strategy
            estrategia.validar(medioPago, pago);
            
            // 4) Calcular el importe final del pago
            BigDecimal montoBase = BigDecimal.valueOf(medioPago.getMonto());
            BigDecimal importeFinal = estrategia.calcularImporteFinal(montoBase, medioPago);
            
            // Actualizar el monto del medio de pago con el importe final calculado
            medioPago.setMonto(importeFinal.floatValue());
            
            sumaCalculada = sumaCalculada.add(importeFinal);
            listaMedios.add(medioPago);
        }

        // Validar que la suma de los medios (con importes finales calculados) coincida con el monto del pago
        BigDecimal montoPago = BigDecimal.valueOf(dto.getMonto());
        BigDecimal diferencia = sumaCalculada.subtract(montoPago).abs();
        
        // Tolerancia de 0.01 para diferencias por redondeo
        if (diferencia.compareTo(new BigDecimal("0.01")) > 0) {
            throw new IllegalArgumentException(
                String.format("La suma de los medios calculados (%.2f) no coincide con el monto del pago (%.2f). " +
                            "Diferencia: %.2f",
                            sumaCalculada.floatValue(),
                            montoPago.floatValue(),
                            diferencia.floatValue())
            );
        }

        // 5) Asignar los medios de pago y el monto calculado al pago
        pago.setMonto(sumaCalculada.floatValue()); // Usar la suma calculada
        pago.setListamediodepago(listaMedios);

        factura.setPago(pago);
        
        // Registrar observer antes de pagar (patrón Observer)
        factura.registrarObserver(new PagoFacturaObserver());
        
        // Pagar factura (usa State para validar, luego notifica observers)
        factura.pagar();

        pagoRepository.save(pago);
        facturaRepository.save(factura);

        return "Pago registrado correctamente.";
    }


    private MedioDePago convertirDTO(MedioDePagoDTO dto) {

        if (dto.getTipo() == null) {
            throw new IllegalArgumentException("Cada medio debe tener el campo 'tipo'.");
        }

        return switch (dto.getTipo().toUpperCase()) {

            case "CHEQUE" -> {
                Cheque c = new Cheque();
                c.setMonto(dto.getMonto());
                c.setFecha(dto.getFecha());
                ChequeDTO d = (ChequeDTO) dto;
                c.setNumeroCheque(d.getNumeroCheque());
                c.setBanco(d.getBanco());
                c.setPlazo(d.getPlazo());
                yield c;
            }

            case "TARJETA_CREDITO" -> {
                TarjetaCredito tc = new TarjetaCredito();
                tc.setMonto(dto.getMonto());
                tc.setFecha(dto.getFecha());
                TarjetaCreditoDTO d = (TarjetaCreditoDTO) dto;
                tc.setBanco(d.getBanco());
                tc.setCuotas(d.getCuotas());
                yield tc;
            }

            case "TARJETA_DEBITO" -> {
                TarjetaDebito td = new TarjetaDebito();
                td.setMonto(dto.getMonto());
                td.setFecha(dto.getFecha());
                TarjetaDebitoDTO d = (TarjetaDebitoDTO) dto;
                td.setBanco(d.getBanco());
                td.setDniTitular(d.getDniTitular());
                yield td;
            }

            case "MONEDA_LOCAL" -> {
                MonedaLocal ml = new MonedaLocal();
                ml.setMonto(dto.getMonto());
                ml.setFecha(dto.getFecha());
                MonedaLocalDTO d = (MonedaLocalDTO) dto;
                ml.setTipoMoneda(d.getTipoMoneda());
                yield ml;
            }

            case "MONEDA_EXTRANJERA" -> {
                MonedaExtranjera me = new MonedaExtranjera();
                me.setMonto(dto.getMonto());
                me.setFecha(dto.getFecha());
                MonedaExtranjeraDTO d = (MonedaExtranjeraDTO) dto;
                me.setTipoMoneda(d.getTipoMoneda());
                yield me;
            }

            default -> throw new IllegalArgumentException("Tipo no reconocido: " + dto.getTipo());
        };
    }
}
