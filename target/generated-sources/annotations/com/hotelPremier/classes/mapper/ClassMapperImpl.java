package com.hotelPremier.classes.mapper;

import com.hotelPremier.classes.estadia.Estadia;
import com.hotelPremier.classes.estadia.EstadiaDTO;
import com.hotelPremier.classes.huesped.Huesped;
import com.hotelPremier.classes.huesped.HuespedDTO;
import com.hotelPremier.classes.reserva.Reserva;
import com.hotelPremier.classes.reserva.ReservaDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-25T11:45:44-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class ClassMapperImpl implements ClassMapper {

    @Override
    public ReservaDTO toDTO(Reserva reserva) {
        if ( reserva == null ) {
            return null;
        }

        ReservaDTO reservaDTO = new ReservaDTO();

        reservaDTO.setTelefono( reserva.getTelefono() );
        reservaDTO.setNombre( reserva.getNombre() );
        reservaDTO.setApellido( reserva.getApellido() );
        reservaDTO.setEstado( reserva.getEstado() );

        return reservaDTO;
    }

    @Override
    public Reserva toEntity(ReservaDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Reserva reserva = new Reserva();

        return reserva;
    }

    @Override
    public List<ReservaDTO> toDTOs(List<Huesped> reservas) {
        if ( reservas == null ) {
            return null;
        }

        List<ReservaDTO> list = new ArrayList<ReservaDTO>( reservas.size() );
        for ( Huesped huesped : reservas ) {
            list.add( huespedToReservaDTO( huesped ) );
        }

        return list;
    }

    @Override
    public HuespedDTO toDTO(Huesped huesped) {
        if ( huesped == null ) {
            return null;
        }

        HuespedDTO huespedDTO = new HuespedDTO();

        huespedDTO.setHuespedID( huesped.getHuespedID() );
        huespedDTO.setNombre( huesped.getNombre() );
        huespedDTO.setApellido( huesped.getApellido() );
        huespedDTO.setTipoDocumento( huesped.getTipoDocumento() );
        huespedDTO.setDni( huesped.getDni() );
        huespedDTO.setFechaNacimiento( huesped.getFechaNacimiento() );
        huespedDTO.setTelefono( huesped.getTelefono() );
        huespedDTO.setEmail( huesped.getEmail() );
        huespedDTO.setCuit( huesped.getCuit() );
        huespedDTO.setPosicionIva( huesped.getPosicionIva() );
        huespedDTO.setOcupacion( huesped.getOcupacion() );
        huespedDTO.setNacionalidad( huesped.getNacionalidad() );
        huespedDTO.setListaEstadia( estadiaListToEstadiaDTOList( huesped.getListaEstadia() ) );

        return huespedDTO;
    }

    @Override
    public Huesped toEntity(HuespedDTO huespedDto) {
        if ( huespedDto == null ) {
            return null;
        }

        Huesped huesped = new Huesped();

        huesped.setDni( huespedDto.getDni() );
        huesped.setTipoDocumento( huespedDto.getTipoDocumento() );
        huesped.setNombre( huespedDto.getNombre() );
        huesped.setApellido( huespedDto.getApellido() );
        huesped.setFechaNacimiento( huespedDto.getFechaNacimiento() );
        huesped.setTelefono( huespedDto.getTelefono() );
        huesped.setEmail( huespedDto.getEmail() );
        huesped.setCuit( huespedDto.getCuit() );
        huesped.setPosicionIva( huespedDto.getPosicionIva() );
        huesped.setOcupacion( huespedDto.getOcupacion() );
        huesped.setNacionalidad( huespedDto.getNacionalidad() );

        return huesped;
    }

    @Override
    public List<HuespedDTO> toDtos(List<Huesped> huesped) {
        if ( huesped == null ) {
            return null;
        }

        List<HuespedDTO> list = new ArrayList<HuespedDTO>( huesped.size() );
        for ( Huesped huesped1 : huesped ) {
            list.add( toDTO( huesped1 ) );
        }

        return list;
    }

    protected ReservaDTO huespedToReservaDTO(Huesped huesped) {
        if ( huesped == null ) {
            return null;
        }

        ReservaDTO reservaDTO = new ReservaDTO();

        reservaDTO.setTelefono( huesped.getTelefono() );
        reservaDTO.setNombre( huesped.getNombre() );
        reservaDTO.setApellido( huesped.getApellido() );

        return reservaDTO;
    }

    protected EstadiaDTO estadiaToEstadiaDTO(Estadia estadia) {
        if ( estadia == null ) {
            return null;
        }

        EstadiaDTO estadiaDTO = new EstadiaDTO();

        estadiaDTO.setCheckin( estadia.getCheckin() );
        estadiaDTO.setCheckout( estadia.getCheckout() );

        return estadiaDTO;
    }

    protected List<EstadiaDTO> estadiaListToEstadiaDTOList(List<Estadia> list) {
        if ( list == null ) {
            return null;
        }

        List<EstadiaDTO> list1 = new ArrayList<EstadiaDTO>( list.size() );
        for ( Estadia estadia : list ) {
            list1.add( estadiaToEstadiaDTO( estadia ) );
        }

        return list1;
    }
}
