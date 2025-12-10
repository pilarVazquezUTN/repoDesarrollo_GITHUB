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

import com.hotelPremier.repository.FacturaRepository;
import com.hotelPremier.repository.PagoRepository;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private FacturaRepository facturaRepository;


    public String ingresarPago(PagoDTO dto) {

        if (dto == null) throw new IllegalArgumentException("JSON vacío.");
        if (dto.getFactura() == null || dto.getFactura().getID() == null)
            throw new IllegalArgumentException("Factura inválida.");

        Factura factura = facturaRepository.findById(dto.getFactura().getID())
            .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada."));

        if (dto.getMedios() == null || dto.getMedios().isEmpty())
            throw new IllegalArgumentException("Debe enviar al menos un medio de pago.");

        float suma = dto.getMedios().stream().map(m -> m.getMonto()).reduce(0f, Float::sum);

        if (suma != dto.getMonto())
            throw new IllegalArgumentException("La suma de los medios no coincide con el monto.");

        Pago pago = new Pago();
        pago.setFecha(new Date());
        pago.setMonto(dto.getMonto());
        pago.setFactura(factura);

        List<MedioDePago> listaMedios = new ArrayList<>();

        for (MedioDePagoDTO mpDTO : dto.getMedios()) {
            listaMedios.add(convertirDTO(mpDTO));
        }

        pago.setListamediodepago(listaMedios);
        factura.setPago(pago);
        factura.setEstado("PAGADA");

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
