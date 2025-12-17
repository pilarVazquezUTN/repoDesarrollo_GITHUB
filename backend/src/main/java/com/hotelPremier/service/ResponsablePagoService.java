package com.hotelPremier.service;

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
     * Busca el ID de un ResponsablePago.
     * Si viene CUIT, busca en PersonaJuridica.
     * Si no viene CUIT pero viene DNI y tipoDocumento, busca en PersonaFisica.
     * 
     * @param dni DNI del huésped (opcional si viene CUIT)
     * @param tipoDocumento Tipo de documento (opcional si viene CUIT)
     * @param cuit CUIT de la persona jurídica (opcional)
     * @return ID del ResponsablePago encontrado, o null si no se encuentra
     */
    public Integer buscarResponsablePago(String dni, String tipoDocumento, String cuit) {
        
        // Si viene CUIT, buscar en PersonaJuridica
        if (cuit != null && !cuit.trim().isEmpty()) {
            return responsablePagoRepository.findPersonaJuridicaByCuit(cuit)
                    .map(PersonaJuridica::getId)
                    .orElse(null);
        }
        
        // Si no viene CUIT, buscar en PersonaFisica por DNI y tipoDocumento
        if (dni != null && !dni.trim().isEmpty() && 
            tipoDocumento != null && !tipoDocumento.trim().isEmpty()) {
            return responsablePagoRepository.findPersonaFisicaByDniAndTipoDocumento(dni, tipoDocumento)
                    .map(PersonaFisica::getId)
                    .orElse(null);
        }
        
        // Si no se proporcionan los datos necesarios, retornar null
        return null;
    }
}
