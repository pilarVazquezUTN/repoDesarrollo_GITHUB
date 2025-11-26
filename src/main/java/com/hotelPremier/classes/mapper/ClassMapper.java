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



        //ReservaDTO toDTOReserva(Reserva reserva);
      //  Reserva toEntityReserva(ReservaDTO dto);
        //List<ReservaDTO> toDTOsReserva(List<Reserva> reservas);

          @Mapping(target = "tipoHab", expression = "java(h.getTipo())")
          HabitacionDTO toDTOHab(Habitacion h);
           List<HabitacionDTO> toDTOsHabitacion(List<Habitacion> habitaciones);
          //List<HabitacionDTO> toDTOsHabitacion(List<Habitacion> habitaciones);

          //HabitacionDTO toDTOHabitacion(Habitacion habitacion);
         //Habitacion toEntityHabitacion(HabitacionDTO dto);

//         List<Habitacion> toEntityHabitacion(List<HabitacionDTO> dtos);

        //@Mapping(source = "id.tipodocumento", target = "tipodocumento")
        //@Mapping(source = "id.dni", target = "dni")
        @Mapping(source = "huespedID", target = "huespedID")
        HuespedDTO toDTO(Huesped huesped);

        //@Mapping(source = "tipodocumento", target = "id.tipodocumento")
        //@Mapping(source = "dni", target = "id.dni")
        @Mapping(source = "huespedID", target = "huespedID")
        Huesped toEntity(HuespedDTO huespedDto);

        List<HuespedDTO> toDtos(List<Huesped> huesped);


        List<ReservaDTO> toDtosReserva( List<Reserva> reserva );
}
