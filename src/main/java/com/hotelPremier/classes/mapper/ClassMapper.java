package com.hotelPremier.classes.mapper;


import com.hotelPremier.classes.huesped.Huesped;
import com.hotelPremier.classes.huesped.HuespedDTO;
import com.hotelPremier.classes.reserva.Reserva;
import com.hotelPremier.classes.reserva.ReservaDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper (componentModel = "spring")
public interface ClassMapper {

        ReservaDTO toDTO(Reserva reserva);
        Reserva toEntity(ReservaDTO dto);
        List<ReservaDTO> toDTOs(List<Huesped> reservas);

        HuespedDTO toDTO(Huesped huesped);
        Huesped toEntity(HuespedDTO huespedDto);
        List<HuespedDTO> toDtos(List<Huesped> huesped);



}
