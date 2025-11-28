package com.hotelPremier.classes.mapper;


import com.hotelPremier.classes.habitacion.Habitacion;
import com.hotelPremier.classes.habitacion.HabitacionDTO;
import com.hotelPremier.classes.huesped.Huesped;
import com.hotelPremier.classes.huesped.HuespedDTO;
import com.hotelPremier.classes.reserva.Reserva;
import com.hotelPremier.classes.reserva.ReservaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper (componentModel = "spring")
public interface ClassMapper {





          @Mapping(target = "tipoHab", expression = "java(h.getTipo())")
          HabitacionDTO toDTOHab(Habitacion h);
           List<HabitacionDTO> toDTOsHabitacion(List<Habitacion> habitaciones);
           //Habitacion toEntityHabitacion(HabitacionDTO dto);

//         List<Habitacion> toEntityHabitacion(List<HabitacionDTO> dtos);


        @Mapping(source = "huespedID", target = "huespedID")
        HuespedDTO toDTO(Huesped huesped);


        @Mapping(source = "huespedID", target = "huespedID")
        Huesped toEntity(HuespedDTO huespedDto);

        List<HuespedDTO> toDtos(List<Huesped> huesped);


        List<ReservaDTO> toDtosReserva( List<Reserva> reserva );

    @Mapping(target = "nro_habitacion", source = "nro_habitacion.numero")
         ReservaDTO toDTOReserva(Reserva reserva);

    @Mapping(target = "nro_habitacion", ignore = true)
        Reserva toEntityReserva(ReservaDTO dto);

}
