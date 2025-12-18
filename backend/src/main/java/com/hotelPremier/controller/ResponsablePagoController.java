package com.hotelPremier.controller;

import com.hotelPremier.service.ResponsablePagoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/responsablesPago")
@Tag(name = "Responsables de Pago", description = "Búsqueda de responsables de pago")
public class ResponsablePagoController {

    @Autowired
    private ResponsablePagoService responsablePagoService;

    @Operation(
        summary = "Buscar responsable de pago",
        description = "Busca el ID de un responsable de pago. Si viene CUIT busca en PersonaJuridica, " +
                      "si no viene CUIT pero viene DNI y tipoDocumento busca en PersonaFisica."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Responsable de pago encontrado"),
        @ApiResponse(responseCode = "404", description = "No se encontró responsable de pago")
    })
    @GetMapping
    public ResponseEntity<ResponsablePagoIdResponse> buscarResponsablePago(
        @Parameter(description = "DNI del huésped (requerido si no viene CUIT)", example = "40991234")
        @RequestParam(required = false) String dni,
        
        @Parameter(description = "Tipo de documento (requerido si no viene CUIT)", example = "DNI")
        @RequestParam(required = false) String tipoDocumento,
        
        @Parameter(description = "CUIT de la persona jurídica (opcional)", example = "20-12345678-9")
        @RequestParam(required = false) String cuit
    ) {
        var result = responsablePagoService.buscarResponsablePago(dni, tipoDocumento, cuit);
        return ResponseEntity.ok(new ResponsablePagoIdResponse(result.id(), result.razonSocial()));
    }
    
    /**
     * Clase interna para devolver el ID y razón social (si aplica) en la respuesta JSON.
     */
    private static class ResponsablePagoIdResponse {
        private Integer id;
        private String razonSocial;
        
        public ResponsablePagoIdResponse(Integer id, String razonSocial) {
            this.id = id;
            this.razonSocial = razonSocial;
        }
        
        public Integer getId() {
            return id;
        }
        
        public void setId(Integer id) {
            this.id = id;
        }
        
        public String getRazonSocial() {
            return razonSocial;
        }
        
        public void setRazonSocial(String razonSocial) {
            this.razonSocial = razonSocial;
        }
    }
}
