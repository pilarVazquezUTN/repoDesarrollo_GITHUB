package com.hotelPremier.classes.mapper;

import com.hotelPremier.classes.direccion.Direccion;
import com.hotelPremier.classes.direccion.DireccionDTO;
import com.hotelPremier.classes.estadia.Estadia;
import com.hotelPremier.classes.estadia.EstadiaDTO;
import com.hotelPremier.classes.habitacion.Habitacion;
import com.hotelPremier.classes.habitacion.HabitacionDTO;
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
    date = "2025-11-30T12:22:55-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class ClassMapperImpl implements ClassMapper {

    @Override
    public HabitacionDTO toDTOHab(Habitacion h) {
        if ( h == null ) {
            return null;
        }

        HabitacionDTO habitacionDTO = new HabitacionDTO();

        if ( h.getNumero() != null ) {
            habitacionDTO.setNumero( h.getNumero() );
        }
        habitacionDTO.setEstado( h.getEstado() );
        habitacionDTO.setPrecio( h.getPrecio() );
        habitacionDTO.setCantidadPersonas( h.getCantidadPersonas() );

        habitacionDTO.setTipohabitacion( h.getTipo() );

        return habitacionDTO;
    }

    @Override
    public List<HabitacionDTO> toDTOsHabitacion(List<Habitacion> habitaciones) {
        if ( habitaciones == null ) {
            return null;
        }

        List<HabitacionDTO> list = new ArrayList<HabitacionDTO>( habitaciones.size() );
        for ( Habitacion habitacion : habitaciones ) {
            list.add( toDTOHab( habitacion ) );
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
        huespedDTO.setFechaNacimiento( huesped.getFechaNacimiento() );
        huespedDTO.setTelefono( huesped.getTelefono() );
        huespedDTO.setEmail( huesped.getEmail() );
        huespedDTO.setdireccion( direccionToDireccionDTO( huesped.getDireccion() ) );
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

        huesped.setHuespedID( huespedDto.getHuespedID() );
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
        huesped.setDireccion( direccionDTOToDireccion( huespedDto.getdireccion() ) );
        huesped.setListaEstadia( estadiaDTOListToEstadiaList( huespedDto.getListaEstadia() ) );

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

    @Override
    public List<ReservaDTO> toDtosReserva(List<Reserva> reserva) {
        if ( reserva == null ) {
            return null;
        }

        List<ReservaDTO> list = new ArrayList<ReservaDTO>( reserva.size() );
        for ( Reserva reserva1 : reserva ) {
            list.add( toDTOReserva( reserva1 ) );
        }

        return list;
    }

    @Override
    public ReservaDTO toDTOReserva(Reserva reserva) {
        if ( reserva == null ) {
            return null;
        }

        ReservaDTO reservaDTO = new ReservaDTO();

        Integer numero = reservaNro_habitacionNumero( reserva );
        if ( numero != null ) {
            reservaDTO.setNro_habitacion( numero );
        }
        reservaDTO.setTelefono( reserva.getTelefono() );
        reservaDTO.setNombre( reserva.getNombre() );
        reservaDTO.setApellido( reserva.getApellido() );
        reservaDTO.setEstado( reserva.getEstado() );
        reservaDTO.setFecha_desde( reserva.getFecha_desde() );
        reservaDTO.setFecha_hasta( reserva.getFecha_hasta() );

        return reservaDTO;
    }

    @Override
    public Reserva toEntityReserva(ReservaDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Reserva reserva = new Reserva();

        reserva.setNombre( dto.getNombre() );
        reserva.setFecha_desde( dto.getFecha_desde() );
        reserva.setApellido( dto.getApellido() );
        reserva.setTelefono( dto.getTelefono() );
        reserva.setEstado( dto.getEstado() );
        reserva.setFecha_hasta( dto.getFecha_hasta() );

        return reserva;
    }

    protected DireccionDTO direccionToDireccionDTO(Direccion direccion) {
        if ( direccion == null ) {
            return null;
        }

        DireccionDTO direccionDTO = new DireccionDTO();

        direccionDTO.setID( direccion.getID() );
        direccionDTO.setCalle( direccion.getCalle() );
        direccionDTO.setNumero( direccion.getNumero() );
        direccionDTO.setLocalidad( direccion.getLocalidad() );
        direccionDTO.setDepartamento( direccion.getDepartamento() );
        direccionDTO.setPiso( direccion.getPiso() );
        direccionDTO.setCodigoPostal( direccion.getCodigoPostal() );
        direccionDTO.setProvincia( direccion.getProvincia() );
        direccionDTO.setPais( direccion.getPais() );

        return direccionDTO;
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

    protected Direccion direccionDTOToDireccion(DireccionDTO direccionDTO) {
        if ( direccionDTO == null ) {
            return null;
        }

        Direccion direccion = new Direccion();

        direccion.setID( direccionDTO.getID() );
        direccion.setCalle( direccionDTO.getCalle() );
        direccion.setNumero( direccionDTO.getNumero() );
        direccion.setLocalidad( direccionDTO.getLocalidad() );
        direccion.setDepartamento( direccionDTO.getDepartamento() );
        direccion.setPiso( direccionDTO.getPiso() );
        direccion.setCodigoPostal( direccionDTO.getCodigoPostal() );
        direccion.setProvincia( direccionDTO.getProvincia() );
        direccion.setPais( direccionDTO.getPais() );

        return direccion;
    }

    protected Estadia estadiaDTOToEstadia(EstadiaDTO estadiaDTO) {
        if ( estadiaDTO == null ) {
            return null;
        }

        Estadia estadia = new Estadia();

        return estadia;
    }

    protected List<Estadia> estadiaDTOListToEstadiaList(List<EstadiaDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<Estadia> list1 = new ArrayList<Estadia>( list.size() );
        for ( EstadiaDTO estadiaDTO : list ) {
            list1.add( estadiaDTOToEstadia( estadiaDTO ) );
        }

        return list1;
    }

    private Integer reservaNro_habitacionNumero(Reserva reserva) {
        if ( reserva == null ) {
            return null;
        }
        Habitacion nro_habitacion = reserva.getNro_habitacion();
        if ( nro_habitacion == null ) {
            return null;
        }
        Integer numero = nro_habitacion.getNumero();
        if ( numero == null ) {
            return null;
        }
        return numero;
    }
}
