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
     * Registra un pago validando medios e importes.
     */
    public String ingresarPago(PagoDTO dto) {

        if (dto == null) throw new IllegalArgumentException("JSON vacío.");
        if (dto.getFactura() == null || dto.getFactura().getID() == null)
            throw new IllegalArgumentException("Factura inválida.");

        Factura factura = facturaRepository.findById(dto.getFactura().getID())
            .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada."));

        if (dto.getMedios() == null || dto.getMedios().isEmpty())
            throw new IllegalArgumentException("Debe enviar al menos un medio de pago.");

        if (pagoRepository.existsByFactura(factura)) {
            throw new RuntimeException("La factura ya posee un pago registrado");
        }

        List<MedioDePago> listaMedios = new ArrayList<>();
        BigDecimal sumaCalculada = BigDecimal.ZERO;

        Pago pago = new Pago();
        pago.setFecha(new Date());
        pago.setFactura(factura);

        for (MedioDePagoDTO mpDTO : dto.getMedios()) {

            MedioDePago medioPago = convertirDTO(mpDTO);

            MedioPagoStrategy estrategia =
                    SelectorMedioPagoStrategy.seleccionarEstrategia(medioPago);

            estrategia.validar(medioPago, pago);

            BigDecimal montoBase = BigDecimal.valueOf(medioPago.getMonto());
            BigDecimal importeFinal = estrategia.calcularImporteFinal(montoBase, medioPago);

            medioPago.setMonto(importeFinal.floatValue());

            sumaCalculada = sumaCalculada.add(importeFinal);
            listaMedios.add(medioPago);
        }

        BigDecimal montoPago = BigDecimal.valueOf(dto.getMonto());
        
        // Validar que el monto del pago no sea mayor que la suma calculada
        // Se permite pagar de más (el cambio se devuelve), pero no se permite pagar de menos
        if (montoPago.compareTo(sumaCalculada) > 0) {
            BigDecimal diferencia = montoPago.subtract(sumaCalculada);
            // Permitir una pequeña tolerancia para redondeos (0.01)
            if (diferencia.compareTo(new BigDecimal("0.01")) > 0) {
                throw new IllegalArgumentException(
                    String.format(
                        "El monto del pago (%.2f) no puede ser mayor que la suma de los medios calculados (%.2f).",
                        montoPago.floatValue(),
                        sumaCalculada.floatValue()
                    )
                );
            }
        }

        // El monto del pago registrado es el monto recibido (puede ser menor o igual a la suma calculada)
        pago.setMonto(montoPago.floatValue());
        pago.setListamediodepago(listaMedios);

        factura.setPago(pago);
        prepararFacturaParaPago(factura);
        factura.pagar();

        pagoRepository.save(pago);
        facturaRepository.save(factura);

        return "Pago registrado correctamente.";
    }

    /**
     * Convierte un DTO de medio de pago en su entidad correspondiente.
     */
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
                me.setCotizacion(d.getCotizacion());
                yield me;
            }

            default -> throw new IllegalArgumentException("Tipo no reconocido: " + dto.getTipo());
        };
    }

    /**
     * Registra los observers necesarios para pagar una factura.
     */
    private void prepararFacturaParaPago(Factura factura) {
        factura.registrarObserver(new PagoFacturaObserver());
    }
}
