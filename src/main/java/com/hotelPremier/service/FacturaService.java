package com.hotelPremier.service;

import com.hotelPremier.classes.DTO.FacturaDTO;
import com.hotelPremier.classes.Dominio.Estadia;
import com.hotelPremier.classes.Dominio.Factura;
import com.hotelPremier.classes.Dominio.NotaDeCredito;
import com.hotelPremier.classes.Dominio.responsablePago.PersonaFisica;
import com.hotelPremier.classes.Dominio.responsablePago.PersonaJuridica;
import com.hotelPremier.classes.Dominio.responsablePago.ResponsablePago;
import com.hotelPremier.repository.EstadiaRepository;
import com.hotelPremier.repository.FacturaRepository;
import com.hotelPremier.repository.ResponsablePagoRepository;
import com.hotelPremier.classes.mapper.ClassMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private EstadiaRepository estadiaRepository;

    @Autowired
    private ResponsablePagoRepository responsablePagoRepository;

    @Autowired
    private ClassMapper mapper;

    // ===========================================
    // CU07 - Facturas por número de habitación
    // ===========================================
    public List<FacturaDTO> obtenerFacturasPorHabitacion(Integer nroHabitacion) {

        List<Factura> lista = facturaRepository.findAll().stream()
                .filter(f -> f.getEstadia() != null &&
                             f.getEstadia().getHabitacion() != null &&
                             "ENCURSO".equals(f.getEstadia().getEstado()) &&
                             f.getEstadia().getHabitacion().getNumero().equals(nroHabitacion))
                .toList();

        return lista.stream()
                .map(mapper::toDTOFactura)
                .toList();
    }

    // ===========================================
    // CU07 - Actualizar factura con ID dentro del DTO
    // ===========================================
    public void actualizarFactura(FacturaDTO dto) {

        if (dto.getID() == null) {
            throw new IllegalArgumentException("El ID de la factura es obligatorio.");
        }

        Factura factura = facturaRepository.findById(dto.getID())
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada."));

        if (dto.getEstado() != null)
            factura.setEstado(dto.getEstado());

        if (dto.getTotal() > 0)
            factura.setTotal(dto.getTotal());

        if (dto.getTipo() != null)
            factura.setTipo(dto.getTipo());

        facturaRepository.save(factura);
    }

    // ===========================================
    //   BUSCAR FACTURAS POR DNI (Persona Física)
    // ===========================================
    public List<FacturaDTO> buscarPorDni(String dni) {

        List<Factura> lista = facturaRepository.buscarPorDni(dni);

        return lista.stream()
                .map(mapper::toDTOFactura)
                .toList();
    }

    // ===========================================
    //   FILTRO GENERAL: CUIT, TipoDoc, NumeroDoc
    // ===========================================
    public List<FacturaDTO> filtrarFacturas(String cuit, String tipoDocumento, String numeroDocumento) {

        List<Factura> lista = facturaRepository.findAll();

        List<Factura> filtradas = lista.stream()
                .filter(f -> coincideCuit(f, cuit))
                .filter(f -> coincideTipoDoc(f, tipoDocumento))
                .filter(f -> coincideNumeroDoc(f, numeroDocumento))
                .toList();

        return filtradas.stream()
                .map(mapper::toDTOFactura)
                .toList();
    }

    // ============================================================
    //     MÉTODOS AUXILIARES DE FILTRADO
    // ============================================================

    private boolean coincideCuit(Factura f, String cuit) {
        if (cuit == null) return true;

        ResponsablePago rp = f.getResponsablePago();
        if (rp instanceof PersonaJuridica pj) {
            return cuit.equals(pj.getCuit());
        }
        return false;
    }

    private boolean coincideTipoDoc(Factura f, String tipoDoc) {
        if (tipoDoc == null) return true;

        ResponsablePago rp = f.getResponsablePago();
        if (rp instanceof PersonaFisica pf) {
            return pf.getHuesped()
                    .getHuespedID()
                    .getTipoDocumento()
                    .equalsIgnoreCase(tipoDoc);
        }
        return false;
    }

    private boolean coincideNumeroDoc(Factura f, String numeroDoc) {
        if (numeroDoc == null) return true;

        ResponsablePago rp = f.getResponsablePago();
        if (rp instanceof PersonaFisica pf) {
            return pf.getHuesped()
                    .getHuespedID()
                    .getDni()
                    .equalsIgnoreCase(numeroDoc);
        }
        return false;
    }

    // ============================================================
    //   CREAR FACTURA (POST /facturas)
    // ============================================================
    public FacturaDTO crearFactura(FacturaDTO dto) {

    Factura factura = mapper.toEntityFactura(dto);

    // 1) Resolver ESTADIA
    if (dto.getEstadia() != null && dto.getEstadia().getID() != null) {
        Estadia est = estadiaRepository.findById(dto.getEstadia().getID())
                            .orElseThrow(() -> new RuntimeException("Estadia no encontrada"));

        factura.setEstadia(est);
    }

    // 2) Resolver RESPONSABLE DE PAGO
    if (dto.getResponsablepago() != null && dto.getResponsablepago().getId() != null) {
        ResponsablePago rp = responsablePagoRepository.findById(dto.getResponsablepago().getId())
                            .orElseThrow(() -> new RuntimeException("ResponsablePago no encontrado"));

        factura.setResponsablePago(rp);
    }

    facturaRepository.save(factura);

    return mapper.toDTOFactura(factura);
}

}
