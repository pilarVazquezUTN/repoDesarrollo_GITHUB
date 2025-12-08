package com.hotelPremier.classes.mapper;


import com.hotelPremier.classes.DTO.EstadiaDTO;
import com.hotelPremier.classes.DTO.HabitacionDTO;
import com.hotelPremier.classes.DTO.HuespedDTO;
import com.hotelPremier.classes.DTO.ReservaDTO;
import com.hotelPremier.classes.Dominio.Estadia;
import com.hotelPremier.classes.Dominio.Habitacion;
import com.hotelPremier.classes.Dominio.Huesped;
import com.hotelPremier.classes.Dominio.Reserva;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper (componentModel = "spring")
public interface ClassMapper {


          Estadia toEntity(EstadiaDTO estadiaDTO);

           @Mapping(target = "tipohabitacion", expression = "java(h.getTipo())")
           HabitacionDTO toDTOHab(Habitacion h);
           List<HabitacionDTO> toDTOsHabitacion(List<Habitacion> habitaciones);
           //Habitacion toEntityHabitacion(HabitacionDTO dto);

//         List<Habitacion> toEntityHabitacion(List<HabitacionDTO> dtos);

        List<EstadiaDTO> toDTOsEstadia(List<Estadia> estadias);

        @Mapping(source = "huespedID", target = "huespedID")
        HuespedDTO toDTO(Huesped huesped);


        @Mapping(source = "huespedID", target = "huespedID")
        Huesped toEntity(HuespedDTO huespedDto);

        List<HuespedDTO> toDtos(List<Huesped> huesped);


        List<ReservaDTO> toDtosReserva( List<Reserva> reserva );

    
         ReservaDTO toDTOReserva(Reserva reserva);

    
        Reserva toEntityReserva(ReservaDTO dto);
        
        List<Reserva> toEntityReservaLista(List<ReservaDTO> lista);


        //ESTADIA
        //List<EstadiaDTO> toDtoEstadia(List<Estadia> listaestadia);
        List<Estadia> toEntityEstadia(List<EstadiaDTO> listaestadiaDTO);


}
