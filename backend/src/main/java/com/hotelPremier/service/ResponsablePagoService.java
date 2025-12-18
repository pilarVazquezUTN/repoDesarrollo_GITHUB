package com.hotelPremier.service;

import com.hotelPremier.classes.exception.RecursoNoEncontradoException;
import com.hotelPremier.repository.ResponsablePagoRepository;
import com.hotelPremier.classes.Dominio.responsablePago.PersonaFisica;
import com.hotelPremier.classes.Dominio.responsablePago.PersonaJuridica;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResponsablePagoService {

    @Autowired
    private ResponsablePagoRepository responsablePagoRepository;

    /**
     * Record para devolver el resultado de la búsqueda de responsable de pago.
     * Incluye el ID y la razón social (solo para PersonaJuridica).
     */
    public record ResponsablePagoResult(Integer id, String razonSocial) {}

    /**
     * Busca el ID de un ResponsablePago.
     * Si viene CUIT, busca en PersonaJuridica.
     * Si no viene CUIT pero viene DNI y tipoDocumento, busca en PersonaFisica.
     * 
     * @param dni DNI del huésped (opcional si viene CUIT)
     * @param tipoDocumento Tipo de documento (opcional si viene CUIT)
     * @param cuit CUIT de la persona jurídica (opcional)
     * @return ResponsablePagoResult con el ID y razón social (si aplica)
     * @throws IllegalArgumentException si no se proporcionan los datos necesarios
     * @throws RecursoNoEncontradoException si no se encuentra el responsable de pago
     */
    public ResponsablePagoResult buscarResponsablePago(String dni, String tipoDocumento, String cuit) {
        
        // Si viene CUIT, buscar en PersonaJuridica
        if (cuit != null && !cuit.trim().isEmpty()) {
            PersonaJuridica pj = responsablePagoRepository.findPersonaJuridicaByCuit(cuit)
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró responsable de pago con CUIT: " + cuit
                    ));
            return new ResponsablePagoResult(pj.getId(), pj.getRazonSocial());
        }
        
        // Si no viene CUIT, buscar en PersonaFisica por DNI y tipoDocumento
        if (dni != null && !dni.trim().isEmpty() && 
            tipoDocumento != null && !tipoDocumento.trim().isEmpty()) {
            PersonaFisica pf = responsablePagoRepository.findPersonaFisicaByDniAndTipoDocumento(dni, tipoDocumento)
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                        String.format("No se encontró responsable de pago con DNI: %s y tipo de documento: %s", dni, tipoDocumento)
                    ));
            return new ResponsablePagoResult(pf.getId(), null);
        }
        
        // Si no se proporcionan los datos necesarios
        throw new IllegalArgumentException(
            "Debe proporcionar CUIT o bien DNI y tipoDocumento para buscar el responsable de pago"
        );
    }
}
