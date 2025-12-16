package com.hotelPremier.classes.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.hotelPremier.classes.DTO.*;
import com.hotelPremier.classes.Dominio.*;
import com.hotelPremier.classes.Dominio.responsablePago.PersonaFisica;
import com.hotelPremier.classes.Dominio.responsablePago.PersonaJuridica;
import com.hotelPremier.classes.Dominio.responsablePago.ResponsablePago;
import com.hotelPremier.classes.Dominio.servicioExtra.ServicioExtra;
import com.hotelPremier.classes.Dominio.servicioExtra.ServicioExtraID;

@Mapper(componentModel = "spring")
public interface ClassMapper {

    // =====================================================
    // HABITACION
    // =====================================================

    HabitacionDTO toDTOHab(Habitacion h);
    List<HabitacionDTO> toDTOsHabitacion(List<Habitacion> h);

    // =====================================================
    // HUESPED
    // =====================================================

    @Mappings({
        @Mapping(target = "huespedID", source = "huespedID"),
        @Mapping(target = "direccion", source = "direccion"),
        @Mapping(target = "cuit", source = "cuit"),
        @Mapping(target = "posicionIva", source = "posicionIva"),
        @Mapping(target = "ocupacion", source = "ocupacion"),
        @Mapping(target = "nacionalidad", source = "nacionalidad")
    })
    HuespedDTO toDTO(Huesped h);

    List<HuespedDTO> toDtos(List<Huesped> h);

    @Mappings({
        @Mapping(target = "listaEstadia", ignore = true),
        @Mapping(target = "responsablePago", ignore = true)
    })
    Huesped toEntity(HuespedDTO dto);


    // =====================================================
    // HUESPED ID
    // =====================================================

    HuespedIDDTO toDTO(HuespedID id);
    HuespedID toEntity(HuespedIDDTO dto);


    // =====================================================
    // ESTADIA
    // =====================================================

    @Mappings({
        @Mapping(target = "habitacion", ignore = true),
        @Mapping(target = "listahuesped", source = "listahuesped"),
        @Mapping(target = "listafactura", ignore = true),
        @Mapping(target = "listaconsumos", ignore = true),
        @Mapping(target = "reserva", ignore = true),
        @Mapping(target = "ID", ignore = true)
    })
    EstadiaDTO toDTOEstadia(Estadia e);

    List<EstadiaDTO> toDTOsEstadia(List<Estadia> lista);

    @Mappings({
        @Mapping(target = "habitacion", ignore = true),
        @Mapping(target = "listahuesped", ignore = true),
        @Mapping(target = "listafactura", ignore = true),
        @Mapping(target = "reserva", ignore = true)
    })
    Estadia toEntity(EstadiaDTO dto);


    // =====================================================
    // RESERVA
    // =====================================================

    ReservaDTO toDTOReserva(Reserva r);
    List<ReservaDTO> toDtosReserva(List<Reserva> r);

    @Mappings({
        @Mapping(target = "habitacion", ignore = true),
        @Mapping(target = "estadia", ignore = true)
    })
    Reserva toEntityReserva(ReservaDTO dto);


    // =====================================================
    // FACTURA
    // =====================================================

    @Mappings({
        @Mapping(target = "estadia", ignore = true),          // Se resuelve en service
        @Mapping(target = "pago", ignore = true),             // Se resuelve en service
        @Mapping(target = "notaDeCredito", ignore = true),    // Se resuelve en service
        @Mapping(target = "responsablePago", ignore = true)   // Se resuelve en service
    })
    Factura toEntityFactura(FacturaDTO dto);

    @Mappings({
        @Mapping(target = "estadia", source = "estadia"),
        @Mapping(target = "notacredito", source = "notaDeCredito"),
        @Mapping(target = "responsablepago", expression = "java(toDTO(f.getResponsablePago()))")
    })
    FacturaDTO toDTOFactura(Factura f);

    List<FacturaDTO> toDTOsFactura(List<Factura> lista);


    // =====================================================
    // SERVICIO EXTRA
    // =====================================================

    @Mappings({
        @Mapping(target = "servicioExtraID", source = "servicioExtraID")
    })
    ServicioExtraDTO toDTOServicioExtra(ServicioExtra s);

    List<ServicioExtraDTO> toDTOsServicioExtra(List<ServicioExtra> lista);

    ServicioExtraIDDTO toDTOServicioExtraID(ServicioExtraID id);
    ServicioExtraID toEntityServicioExtraID(ServicioExtraIDDTO dto);


    // =====================================================
    // RESPONSABLE DE PAGO (Manual)
    // =====================================================

    default ResponsablePagoDTO toDTO(ResponsablePago r) {

        if (r == null) return null;

        ResponsablePagoDTO dto = new ResponsablePagoDTO();
        dto.setId(r.getId());

        if (r instanceof PersonaFisica pf) {
            dto.setTipo("FISICA");
            dto.setDni(pf.getHuesped().getHuespedID().getDni());
            dto.setTipoDocumento(pf.getHuesped().getHuespedID().getTipoDocumento());
        }

        if (r instanceof PersonaJuridica pj) {
            dto.setTipo("JURIDICA");
            dto.setCuit(pj.getCuit());
        }

        return dto;
    }
}
