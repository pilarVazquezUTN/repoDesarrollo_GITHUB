package com.hotelPremier.classes.mapper;


import com.hotelPremier.classes.huesped.Huesped;
import com.hotelPremier.classes.huesped.HuespedDTO;
import com.hotelPremier.classes.reserva.Reserva;
import com.hotelPremier.classes.reserva.ReservaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper (componentModel = "spring")
public interface ClassMapper {


        ReservaDTO toDTO(Reserva reserva);
        Reserva toEntity(ReservaDTO dto);
        List<ReservaDTO> toDTOs(List<Huesped> reservas);


        //@Mapping(source = "id.tipodocumento", target = "tipodocumento")
        //@Mapping(source = "id.dni", target = "dni")
        @Mapping(source = "huespedID", target = "huespedID")
        HuespedDTO toDTO(Huesped huesped);
        //@Mapping(source = "tipodocumento", target = "id.tipodocumento")
        //@Mapping(source = "dni", target = "id.dni")
        @Mapping(source = "huespedID", target = "huespedID") 
        Huesped toEntity(HuespedDTO huespedDto);
        List<HuespedDTO> toDtos(List<Huesped> huesped);



}
