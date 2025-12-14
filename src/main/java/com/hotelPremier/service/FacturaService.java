package com.hotelPremier.service;

import com.hotelPremier.classes.DTO.FacturaDTO;
import com.hotelPremier.classes.Dominio.Estadia;
import com.hotelPremier.classes.Dominio.Factura;
import com.hotelPremier.classes.Dominio.factura.calculo.CalculoFacturaStrategy;
import com.hotelPremier.classes.Dominio.factura.calculo.DatosFactura;
import com.hotelPremier.classes.Dominio.factura.calculo.SelectorEstrategiaCalculo;
import com.hotelPremier.classes.Dominio.factura.observer.CheckoutFacturaObserver;
import com.hotelPremier.classes.Dominio.factura.observer.PagoFacturaObserver;
import com.hotelPremier.classes.Dominio.factura.observer.NotaCreditoFacturaObserver;
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

    // ===========================================
    // CU07 - Facturas por número de habitación
    // ===========================================
    /**
     * Obtiene las facturas asociadas a estadías en curso para una habitación específica.
     * 
     * Nota: Se listan facturas activas asociadas a estadías en curso (ENCURSO).
     * Normalmente las facturas se consultan post checkout, pero este endpoint
     * permite consultar facturas de estadías activas.
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

    // ===========================================
    // CU07 - Actualizar factura con ID dentro del DTO
    // ===========================================
    public void actualizarFactura(FacturaDTO dto) {

        if (dto.getID() == null) {
            throw new IllegalArgumentException("El ID de la factura es obligatorio.");
        }

        Factura factura = facturaRepository.findById(dto.getID())
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada."));

        // Nota: setEstado() sincroniza automáticamente el estadoFactura (patrón State)
        // Para operaciones controladas, usar factura.pagar(), factura.cancelar(), factura.aplicarNotaCredito()
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
    //   CREAR FACTURA (POST /facturas) - CON PATRÓN STRATEGY
    // ============================================================
    /**
     * Crea una factura usando el patrón Strategy para calcular el total.
     * Valida que el total calculado coincida con el enviado por el frontend.
     * 
     * @param dto DTO con los datos de la factura (incluye total estimado)
     * @param datosFactura Datos adicionales para el cálculo (consumos, hora checkout, etc.)
     * @return DTO de la factura creada
     * @throws IllegalArgumentException si el total calculado no coincide con el estimado
     */
    public FacturaDTO crearFacturaConCalculo(FacturaDTO dto, DatosFactura datosFactura) {
        
        // 1) Obtener la Estadia real desde la base
        if (dto.getEstadia() == null || dto.getEstadia().getID() == null) {
            throw new IllegalArgumentException("La factura debe tener una estadía asociada");
        }
        
        Estadia estadia = estadiaRepository.findById(dto.getEstadia().getID())
                .orElseThrow(() -> new IllegalArgumentException("Estadia no encontrada"));

        // 2) Validar que la estadía permita generar facturas (patrón State)
        Factura factura = mapper.toEntityFactura(dto);
        estadia.generarFactura(factura);

        // 3) Seleccionar la Strategy correcta según horario de checkout
        CalculoFacturaStrategy estrategia = SelectorEstrategiaCalculo.seleccionarEstrategia(estadia, datosFactura);

        // 4) Ejecutar la Strategy para calcular el total real
        BigDecimal totalCalculado = estrategia.calcularTotal(estadia, datosFactura);

        // 5) Comparar el total calculado con el recibido desde el frontend
        if (datosFactura.getTotalEstimado() != null) {
            BigDecimal totalEstimado = BigDecimal.valueOf(datosFactura.getTotalEstimado());
            BigDecimal diferencia = totalCalculado.subtract(totalEstimado).abs();
            
            // Tolerancia de 0.01 para diferencias por redondeo
            if (diferencia.compareTo(new BigDecimal("0.01")) > 0) {
                throw new IllegalArgumentException(
                    String.format("El total calculado (%.2f) no coincide con el estimado (%.2f). " +
                                "Diferencia: %.2f", 
                                totalCalculado.floatValue(), 
                                totalEstimado.floatValue(),
                                diferencia.floatValue())
                );
            }
        }

        // 6) Asignar el total calculado a la factura
        factura.setTotal(totalCalculado.floatValue());
        factura.setEstadia(estadia);
        factura.setFecha(new java.util.Date());

        // 7) Resolver RESPONSABLE DE PAGO
        if (dto.getResponsablepago() != null && dto.getResponsablepago().getId() != null) {
            ResponsablePago rp = responsablePagoRepository.findById(dto.getResponsablepago().getId())
                                .orElseThrow(() -> new IllegalArgumentException("ResponsablePago no encontrado"));
            factura.setResponsablePago(rp);
        }

        // 8) Persistir la factura
        facturaRepository.save(factura);

        return mapper.toDTOFactura(factura);
    }

    // ============================================================
    //   CREAR FACTURA (POST /facturas) - MÉTODO UNIFICADO
    // ============================================================
    /**
     * Crea una factura. Si el DTO incluye datos para cálculo automático (fechaHoraCheckoutReal),
     * usa el patrón Strategy para calcular y validar el total.
     * Si no, usa el método tradicional (compatibilidad hacia atrás).
     */
    public FacturaDTO crearFactura(FacturaDTO dto) {

        // Si tiene fechaHoraCheckoutReal, usar cálculo automático con Strategy
        if (dto.getFechaHoraCheckoutReal() != null && dto.getEstadia() != null && dto.getEstadia().getID() != null) {
            return crearFacturaConCalculoAutomatico(dto);
        }

        // Método tradicional (compatibilidad)
        Factura factura = mapper.toEntityFactura(dto);

        // 1) Resolver ESTADIA y usar patrón State para validar operación
        if (dto.getEstadia() != null && dto.getEstadia().getID() != null) {
            Estadia est = estadiaRepository.findById(dto.getEstadia().getID())
                                .orElseThrow(() -> new RuntimeException("Estadia no encontrada"));

            // Usa el patrón State: valida que la estadía permita generar facturas
            est.generarFactura(factura);
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

    /**
     * Método interno que construye DatosFactura desde el DTO y usa el cálculo automático.
     */
    private FacturaDTO crearFacturaConCalculoAutomatico(FacturaDTO dto) {
        // Construir DatosFactura desde el DTO
        DatosFactura datosFactura = new DatosFactura();
        
        // Obtener consumos seleccionados
        List<ServicioExtra> consumos = new ArrayList<>();
        if (dto.getConsumosIds() != null && !dto.getConsumosIds().isEmpty() && dto.getEstadia() != null) {
            consumos = servicioExtraRepository.findByEstadiaIdAndServicioIds(
                dto.getEstadia().getID(), 
                dto.getConsumosIds()
            );
        }
        datosFactura.setConsumosSeleccionados(consumos);
        
        // Tipo de factura
        datosFactura.setTipoFactura(dto.getTipo());
        
        // Fecha y hora de checkout real
        datosFactura.setFechaHoraCheckoutReal(dto.getFechaHoraCheckoutReal());
        
        // Total estimado
        datosFactura.setTotalEstimado(dto.getTotalEstimado() != null ? dto.getTotalEstimado() : dto.getTotal());

        // Usar el método con Strategy
        return crearFacturaConCalculo(dto, datosFactura);
    }

    // ============================================================
    //   GENERAR FACTURA FINAL CON PATRÓN OBSERVER
    // ============================================================
    /**
     * Genera la factura final de una estadía usando los patrones State y Observer.
     * 
     * El patrón State valida que la transición a GENERADA sea válida.
     * El patrón Observer ejecuta las consecuencias del dominio:
     * - CheckoutFacturaObserver: finaliza estadía y libera habitación
     * 
     * @param facturaId ID de la factura a generar como final
     * @return La factura generada
     * @throws IllegalArgumentException si la factura no existe o no puede generarse
     */
    public FacturaDTO generarFacturaFinal(Integer facturaId) {
        Factura factura = facturaRepository.findById(facturaId)
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada con ID: " + facturaId));

        // Preparar factura para checkout: registrar observers que reaccionarán al cambio de estado
        prepararFacturaParaCheckout(factura);

        // Generar factura final (usa State para validar, luego notifica observers)
        factura.generarFacturaFinal();

        // Persistir cambios (estadía y habitación fueron actualizados por observers)
        facturaRepository.save(factura);
        
        // Persistir estadía si fue actualizada
        if (factura.getEstadia() != null) {
            estadiaRepository.save(factura.getEstadia());
            
            // Persistir habitación si fue actualizada
            if (factura.getEstadia().getHabitacion() != null) {
                habitacionRepository.save(factura.getEstadia().getHabitacion());
            }
        }

        return mapper.toDTOFactura(factura);
    }

    // ============================================================
    //   PAGAR FACTURA CON PATRÓN OBSERVER
    // ============================================================
    /**
     * Paga una factura usando los patrones State y Observer.
     * 
     * El patrón State valida que la transición a PAGADA sea válida.
     * El patrón Observer ejecuta las consecuencias del dominio:
     * - PagoFacturaObserver: valida que el pago esté registrado
     * 
     * @param facturaId ID de la factura a pagar
     * @return La factura pagada
     * @throws IllegalArgumentException si la factura no existe o no puede pagarse
     */
    public FacturaDTO pagarFactura(Integer facturaId) {
        Factura factura = facturaRepository.findById(facturaId)
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada con ID: " + facturaId));

        // Preparar factura para pago: registrar observers que reaccionarán al cambio de estado
        prepararFacturaParaPago(factura);

        // Pagar factura (usa State para validar, luego notifica observers)
        factura.pagar();

        // Persistir cambios
        facturaRepository.save(factura);

        return mapper.toDTOFactura(factura);
    }

    // ============================================================
    //   APLICAR NOTA DE CRÉDITO CON PATRÓN OBSERVER
    // ============================================================
    /**
     * Aplica una nota de crédito a una factura usando los patrones State y Observer.
     * 
     * El patrón State valida que la transición a CANCELADA sea válida.
     * El patrón Observer ejecuta las consecuencias del dominio:
     * - NotaCreditoFacturaObserver: valida que la NC esté asociada y el total sea 0
     * 
     * @param facturaId ID de la factura a cancelar
     * @return La factura cancelada
     * @throws IllegalArgumentException si la factura no existe o no puede cancelarse
     */
    public FacturaDTO aplicarNotaCreditoAFactura(Integer facturaId) {
        Factura factura = facturaRepository.findById(facturaId)
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada con ID: " + facturaId));

        // Preparar factura para nota de crédito: registrar observers que reaccionarán al cambio de estado
        prepararFacturaParaNotaCredito(factura);

        // Aplicar nota de crédito (usa State para validar, luego notifica observers)
        factura.aplicarNotaCredito();

        // Persistir cambios
        facturaRepository.save(factura);

        return mapper.toDTOFactura(factura);
    }

    // ============================================================
    //   MÉTODOS AUXILIARES PARA REGISTRO DE OBSERVERS
    // ============================================================
    /**
     * Prepara la factura para checkout registrando los observers necesarios.
     * El registro de observers está claramente separado del cambio de estado.
     * 
     * @param factura La factura a preparar
     */
    private void prepararFacturaParaCheckout(Factura factura) {
        factura.registrarObserver(new CheckoutFacturaObserver());
    }

    /**
     * Prepara la factura para pago registrando los observers necesarios.
     * El registro de observers está claramente separado del cambio de estado.
     * 
     * @param factura La factura a preparar
     */
    private void prepararFacturaParaPago(Factura factura) {
        factura.registrarObserver(new PagoFacturaObserver());
    }

    /**
     * Prepara la factura para nota de crédito registrando los observers necesarios.
     * El registro de observers está claramente separado del cambio de estado.
     * 
     * @param factura La factura a preparar
     */
    private void prepararFacturaParaNotaCredito(Factura factura) {
        factura.registrarObserver(new NotaCreditoFacturaObserver());
    }

}
