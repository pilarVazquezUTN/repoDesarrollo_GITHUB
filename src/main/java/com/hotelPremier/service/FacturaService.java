package com.hotelPremier.service;

import com.hotelPremier.classes.DTO.FacturaDTO;
import com.hotelPremier.classes.Dominio.Factura;
import com.hotelPremier.repository.FacturaRepository;
import com.hotelPremier.classes.mapper.ClassMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private ClassMapper mapper;

    // ===========================================
    // CU07 - Facturas por número de habitación
    // ===========================================
public List<FacturaDTO> obtenerFacturasPorHabitacion(Integer nroHabitacion) {

    List<Factura> lista = facturaRepository.findAll().stream()
            .filter(f -> f.getEstadia() != null &&
                         f.getEstadia().getHabitacion() != null &&
                         f.getEstadia().getHabitacion().getNumero().equals(nroHabitacion))
            .toList();

    return lista.stream()
            .map(mapper::toDTOFactura)
            .toList();
}


    // ===========================================
    // CU07 - Actualizar factura con ID del DTO
    // ===========================================
    public void actualizarFactura(FacturaDTO dto) {

        if (dto.getID() == null) {
            throw new RuntimeException("El ID de la factura es obligatorio.");
        }

        Factura factura = facturaRepository.findById(dto.getID())
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        if (dto.getEstado() != null)
            factura.setEstado(dto.getEstado());

        if (dto.getTotal() > 0)
            factura.setTotal(dto.getTotal());

        if (dto.getTipo() != null)
            factura.setTipo(dto.getTipo());

        facturaRepository.save(factura);
    }

    public List<FacturaDTO> buscarPorDni(String dni) {

        List<Factura> lista = facturaRepository.buscarPorDni(dni);

        return lista.stream()
                .map(mapper::toDTOFactura)
                .toList();
    }

    public List<FacturaDTO> filtrarFacturas(String cuit, String tipoDocumento, String numeroDocumento) {

    List<Factura> lista = facturaRepository.findAll();

    List<Factura> filtradas = lista.stream()

        // ----------------------------
        //   FILTRAR POR CUIT
        // ----------------------------
        .filter(f -> cuit == null || {
            var rp = f.getResponsablepago();
            if (rp == null) return false;
            if (rp instanceof ResponsableJuridico rj) {
                return cuit.equals(rj.getCuit());
            }
            return false;
        })

        // ----------------------------
        //   FILTRAR POR TIPO DOCUMENTO
        // ----------------------------
        .filter(f -> tipoDocumento == null || {
            var rp = f.getResponsablepago();
            if (rp == null) return false;
            if (rp instanceof ResponsableHuesped rh) {
                return rh.getHuesped().getHuespedID().getTipoDocumento()
                        .equalsIgnoreCase(tipoDocumento);
            }
            return false;
        })

        // ----------------------------
        //   FILTRAR POR NRO DOCUMENTO
        // ----------------------------
        .filter(f -> numeroDocumento == null || {
            var rp = f.getResponsablepago();
            if (rp == null) return false;
            if (rp instanceof ResponsableHuesped rh) {
                return rh.getHuesped().getHuespedID().getDni().equals(numeroDocumento);
            }
            return false;
        })

        .toList();

    return filtradas.stream()
            .map(mapper::toDTOFactura)
            .toList();
}

}
