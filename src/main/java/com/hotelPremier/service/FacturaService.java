package com.hotelPremier.service;

import com.hotelPremier.classes.DTO.FacturaDTO;
import com.hotelPremier.classes.Dominio.Estadia;
import com.hotelPremier.classes.Dominio.Factura;
import com.hotelPremier.classes.Dominio.factura.calculo.CalculoFacturaStrategy;
import com.hotelPremier.classes.Dominio.factura.calculo.DatosFactura;
import com.hotelPremier.classes.Dominio.factura.calculo.SelectorEstrategiaCalculo;
import com.hotelPremier.classes.Dominio.factura.observer.CheckoutFacturaObserver;
import com.hotelPremier.classes.Dominio.factura.observer.NotaCreditoFacturaObserver;
import com.hotelPremier.classes.Dominio.factura.observer.PagoFacturaObserver;
import com.hotelPremier.classes.Dominio.responsablePago.PersonaFisica;
import com.hotelPremier.classes.Dominio.responsablePago.PersonaJuridica;
import com.hotelPremier.classes.Dominio.responsablePago.ResponsablePago;
import com.hotelPremier.classes.Dominio.servicioExtra.ServicioExtra;
import com.hotelPremier.repository.EstadiaRepository;
import com.hotelPremier.repository.FacturaRepository;
import com.hotelPremier.repository.HabitacionRepository;
import com.hotelPremier.repository.ResponsablePagoRepository;
import com.hotelPremier.repository.ServicioExtraRepository;
import com.hotelPremier.classes.mapper.ClassMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    private ServicioExtraRepository servicioExtraRepository;

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private ClassMapper mapper;

    /**
     * Obtiene las facturas de estadías en curso para una habitación.
     */
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

    /**
     * Busca facturas asociadas a un DNI.
     */
    public List<FacturaDTO> buscarPorDni(String dni) {

        List<Factura> lista = facturaRepository.buscarPorDni(dni);

        return lista.stream()
                .map(mapper::toDTOFactura)
                .toList();
    }

    /**
     * Filtra facturas por CUIT o documento.
     */
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

    /**
     * Crea una factura calculando el total con Strategy.
     */
    public FacturaDTO crearFacturaConCalculo(FacturaDTO dto, DatosFactura datosFactura) {

        if (dto.getEstadia() == null || dto.getEstadia().getID() == null) {
            throw new IllegalArgumentException("La factura debe tener una estadía asociada");
        }

        Estadia estadia = estadiaRepository.findById(dto.getEstadia().getID())
                .orElseThrow(() -> new IllegalArgumentException("Estadia no encontrada"));

        Factura factura = mapper.toEntityFactura(dto);
        estadia.generarFactura(factura);

        CalculoFacturaStrategy estrategia =
                SelectorEstrategiaCalculo.seleccionarEstrategia(estadia, datosFactura);

        BigDecimal totalCalculado = estrategia.calcularTotal(estadia, datosFactura);

        if (datosFactura.getTotalEstimado() != null) {
            BigDecimal totalEstimado = BigDecimal.valueOf(datosFactura.getTotalEstimado());
            BigDecimal diferencia = totalCalculado.subtract(totalEstimado).abs();

            if (diferencia.compareTo(new BigDecimal("0.01")) > 0) {
                throw new IllegalArgumentException(
                        String.format(
                                "El total calculado (%.2f) no coincide con el estimado (%.2f). Diferencia: %.2f",
                                totalCalculado.floatValue(),
                                totalEstimado.floatValue(),
                                diferencia.floatValue()
                        )
                );
            }
        }

        factura.setTotal(totalCalculado.floatValue());
        factura.setEstadia(estadia);
        factura.setFecha(new java.util.Date());

        if (dto.getResponsablepago() != null && dto.getResponsablepago().getId() != null) {
            ResponsablePago rp = responsablePagoRepository.findById(dto.getResponsablepago().getId())
                    .orElseThrow(() -> new IllegalArgumentException("ResponsablePago no encontrado"));
            factura.setResponsablePago(rp);
        }

        facturaRepository.save(factura);

        return mapper.toDTOFactura(factura);
    }

    /**
     * Crea una factura usando cálculo automático o método simple.
     */
    public FacturaDTO crearFactura(FacturaDTO dto) {

        if (dto.getFechaHoraCheckoutReal() != null &&
            dto.getEstadia() != null &&
            dto.getEstadia().getID() != null) {
            return crearFacturaConCalculoAutomatico(dto);
        }

        Factura factura = mapper.toEntityFactura(dto);

        if (dto.getEstadia() != null && dto.getEstadia().getID() != null) {
            Estadia est = estadiaRepository.findById(dto.getEstadia().getID())
                    .orElseThrow(() -> new RuntimeException("Estadia no encontrada"));
            est.generarFactura(factura);
        }

        if (dto.getResponsablepago() != null && dto.getResponsablepago().getId() != null) {
            ResponsablePago rp = responsablePagoRepository.findById(dto.getResponsablepago().getId())
                    .orElseThrow(() -> new RuntimeException("ResponsablePago no encontrado"));
            factura.setResponsablePago(rp);
        }

        facturaRepository.save(factura);

        return mapper.toDTOFactura(factura);
    }

    /**
     * Construye los datos de cálculo y genera la factura automáticamente.
     */
    private FacturaDTO crearFacturaConCalculoAutomatico(FacturaDTO dto) {

        DatosFactura datosFactura = new DatosFactura();

        List<ServicioExtra> consumos = new ArrayList<>();
        if (dto.getConsumosIds() != null && !dto.getConsumosIds().isEmpty() && dto.getEstadia() != null) {
            consumos = servicioExtraRepository.findByEstadiaIdAndServicioIds(
                    dto.getEstadia().getID(),
                    dto.getConsumosIds()
            );
        }

        datosFactura.setConsumosSeleccionados(consumos);
        datosFactura.setTipoFactura(dto.getTipo());
        datosFactura.setFechaHoraCheckoutReal(dto.getFechaHoraCheckoutReal());
        datosFactura.setTotalEstimado(dto.getTotalEstimado() != null ? dto.getTotalEstimado() : dto.getTotal());

        return crearFacturaConCalculo(dto, datosFactura);
    }

    /**
     * Genera la factura final de una estadía.
     */
    public FacturaDTO generarFacturaFinal(Integer facturaId) {

        Factura factura = facturaRepository.findById(facturaId)
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada con ID: " + facturaId));

        prepararFacturaParaCheckout(factura);
        factura.generarFacturaFinal();
        facturaRepository.save(factura);

        if (factura.getEstadia() != null) {
            estadiaRepository.save(factura.getEstadia());
            if (factura.getEstadia().getHabitacion() != null) {
                habitacionRepository.save(factura.getEstadia().getHabitacion());
            }
        }

        return mapper.toDTOFactura(factura);
    }

    /**
     * Paga una factura.
     */
    public FacturaDTO pagarFactura(Integer facturaId) {

        Factura factura = facturaRepository.findById(facturaId)
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada con ID: " + facturaId));

        prepararFacturaParaPago(factura);
        factura.pagar();
        facturaRepository.save(factura);

        return mapper.toDTOFactura(factura);
    }

    /**
     * Aplica una nota de crédito a una factura.
     */
    public FacturaDTO aplicarNotaCreditoAFactura(Integer facturaId) {

        Factura factura = facturaRepository.findById(facturaId)
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada con ID: " + facturaId));

        prepararFacturaParaNotaCredito(factura);
        factura.aplicarNotaCredito();
        facturaRepository.save(factura);

        return mapper.toDTOFactura(factura);
    }

    private boolean coincideCuit(Factura f, String cuit) {
        if (cuit == null) return true;
        if (f.getResponsablePago() instanceof PersonaJuridica pj) {
            return cuit.equals(pj.getCuit());
        }
        return false;
    }

    private boolean coincideTipoDoc(Factura f, String tipoDoc) {
        if (tipoDoc == null) return true;
        if (f.getResponsablePago() instanceof PersonaFisica pf) {
            return pf.getHuesped().getHuespedID().getTipoDocumento().equalsIgnoreCase(tipoDoc);
        }
        return false;
    }

    private boolean coincideNumeroDoc(Factura f, String numeroDoc) {
        if (numeroDoc == null) return true;
        if (f.getResponsablePago() instanceof PersonaFisica pf) {
            return pf.getHuesped().getHuespedID().getDni().equalsIgnoreCase(numeroDoc);
        }
        return false;
    }

    private void prepararFacturaParaCheckout(Factura factura) {
        factura.registrarObserver(new CheckoutFacturaObserver());
    }

    private void prepararFacturaParaPago(Factura factura) {
        factura.registrarObserver(new PagoFacturaObserver());
    }

    private void prepararFacturaParaNotaCredito(Factura factura) {
        factura.registrarObserver(new NotaCreditoFacturaObserver());
    }
}
