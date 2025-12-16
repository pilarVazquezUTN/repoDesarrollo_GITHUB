package com.hotelPremier.service;

import com.hotelPremier.classes.Dominio.Factura;
import com.hotelPremier.classes.Dominio.NotaDeCredito;
import com.hotelPremier.classes.DTO.NotaDeCreditoDTO;
import com.hotelPremier.classes.DTO.FacturaDTO;
import com.hotelPremier.classes.Dominio.factura.observer.NotaCreditoFacturaObserver;
import com.hotelPremier.repository.FacturaRepository;
import com.hotelPremier.repository.NotaDeCreditoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotaDeCreditoService {

    @Autowired
    private NotaDeCreditoRepository notaRepo;

    @Autowired
    private FacturaRepository facturaRepo;

    /**
     * Registra una nota de crédito y la aplica a las facturas indicadas.
     */
    public String ingresarNotaDeCredito(NotaDeCreditoDTO dto) {

        if (dto.getImporte() == null || dto.getImporte() <= 0) {
            throw new IllegalArgumentException("El importe debe ser mayor a cero.");
        }

        if (dto.getFacturas() == null || dto.getFacturas().isEmpty()) {
            throw new IllegalArgumentException("Debe indicar al menos una factura.");
        }

        List<Integer> ids = dto.getFacturas()
                .stream()
                .map(FacturaDTO::getID)
                .toList();

        List<Factura> facturas = facturaRepo.findAllById(ids);

        if (facturas.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron facturas válidas.");
        }

        float sumaTotales = facturas.stream()
                .map(Factura::getTotal)
                .reduce(0f, Float::sum);

        if (dto.getImporte() < sumaTotales) {
            throw new IllegalArgumentException(
                "El importe de la nota de crédito es insuficiente. Debe ser al menos: " + sumaTotales
            );
        }

        NotaDeCredito nota = new NotaDeCredito();
        nota.setImporte(dto.getImporte());

        NotaDeCredito guardada = notaRepo.save(nota);

        for (Factura factura : facturas) {
            factura.setNotaDeCredito(guardada);
            prepararFacturaParaNotaCredito(factura);
            factura.aplicarNotaCredito();
            facturaRepo.save(factura);
        }

        return "Nota de crédito aplicada correctamente a " + facturas.size() + " factura(s).";
    }

    /**
     * Registra los observers necesarios para aplicar una nota de crédito.
     */
    private void prepararFacturaParaNotaCredito(Factura factura) {
        factura.registrarObserver(new NotaCreditoFacturaObserver());
    }
}
