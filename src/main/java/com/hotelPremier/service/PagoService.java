package com.hotelPremier.service;

import com.hotelPremier.repository.FacturaRepository;
import com.hotelPremier.repository.PagoRepository;
import com.hotelPremier.classes.DTO.PagoDTO;
import com.hotelPremier.classes.Dominio.Factura;
import com.hotelPremier.classes.Dominio.Pago;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Date;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private FacturaRepository facturaRepository;

    // CU16 - Ingresar Pago
    public String ingresarPago(PagoDTO dto) {
        if (dto.getFactura() != null) {
            System.out.println("FACTURA ID = " + dto.getFactura().getID());
        }

        // 1. Buscar factura
        Factura factura = facturaRepository.findById(dto.getFactura().getID())
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada"));

        // 2. Validar monto
        if (dto.getMonto() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0.");
        }

        if (dto.getMonto() > factura.getTotal()) {
            throw new IllegalArgumentException("El monto supera el total pendiente de la factura.");
        }

        // 3. Crear pago
        Pago pago = new Pago();
        pago.setMonto(dto.getMonto());
        pago.setMedioPago(dto.getMedioPago());
        pago.setFecha(new Date());
        pago.setFactura(factura);

        // 4. Asociar factura → pago
        factura.setPago(pago);
        factura.setEstado("PAGADA");

        // 5. Guardar en BD
        pagoRepository.save(pago);
        facturaRepository.save(factura);

        return "Pago realizado con exito";
    }
}
