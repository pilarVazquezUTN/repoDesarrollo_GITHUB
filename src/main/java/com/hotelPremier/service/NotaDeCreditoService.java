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
     * CU19 - Ingresar Nota de Crédito
     * REGLA: El importe de la NC debe cubrir el total de TODAS las facturas afectadas.
     */
    public String ingresarNotaDeCredito(NotaDeCreditoDTO dto) {

        if (dto.getImporte() == null || dto.getImporte() <= 0) {
            throw new IllegalArgumentException("El importe debe ser mayor a cero.");
        }

        if (dto.getFacturas() == null || dto.getFacturas().isEmpty()) {
            throw new IllegalArgumentException("Debe indicar al menos una factura.");
        }

        // 1️⃣ Obtener IDs desde los FacturaDTO
        List<Integer> ids = dto.getFacturas()
                .stream()
                .map(FacturaDTO::getID)
                .toList();

        // 2️⃣ Buscar las facturas reales
        List<Factura> facturas = facturaRepo.findAllById(ids);

        if (facturas.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron facturas válidas.");
        }

        // 3️⃣ Sumar totales de facturas
        float sumaTotales = facturas.stream()
                .map(Factura::getTotal)
                .reduce(0f, Float::sum);

        // 🔥 REGLA DE NEGOCIO: importe debe ser suficiente
        if (dto.getImporte() < sumaTotales) {
            throw new IllegalArgumentException(
                "El importe de la nota de crédito es insuficiente. " +
                "Debe ser al menos: " + sumaTotales
            );
        }

        // 4️⃣ Crear y guardar la nota de crédito
        NotaDeCredito nota = new NotaDeCredito();
        nota.setImporte(dto.getImporte());

        NotaDeCredito guardada = notaRepo.save(nota);

        // 5️⃣ Aplicar NC a cada factura → total queda en 0
        for (Factura factura : facturas) {
            factura.setNotaDeCredito(guardada);
            
            // Registrar observer antes de aplicar nota de crédito (patrón Observer)
            factura.registrarObserver(new NotaCreditoFacturaObserver());
            
            // Aplicar nota de crédito (usa State para validar, luego notifica observers)
            factura.aplicarNotaCredito();
            
            facturaRepo.save(factura);
        }

        return "Nota de crédito aplicada correctamente a " + facturas.size() + " factura(s).";
    }
}
