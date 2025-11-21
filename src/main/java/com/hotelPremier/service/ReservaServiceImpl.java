package com.hotelPremier.service;

import com.hotelPremier.classes.FuncionesUtiles;
import com.hotelPremier.classes.reserva.Reserva;
import com.hotelPremier.classes.reserva.ReservaDTO;
import com.hotelPremier.repository.HuespedRepositoryDAO;
import com.hotelPremier.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaServiceImpl  {

    @Autowired
    private ReservaRepository reservaRepository;



    //el service debe retornar el dto al controller
    public List<ReservaDTO> findByApellidoContainingIgnoreCase(String apellido) {

        // Si apellido es nulo o está vacío → devolver TODAS las reservas
        if (apellido == null || apellido.trim().isEmpty()) {
            return mapDTOreserva(reservaRepository.findAll());
        }

        // Si viene un apellido  filtrar
        return mapDTOreserva(reservaRepository.findByApellidoContainingIgnoreCase(apellido));
    }


       // return mapDTOreserva(reservaRepository.findByApellidoContainingIgnoreCase(apellido));



    public List<ReservaDTO> mapDTOreserva(List<Reserva> reservas) {

        if (reservas == null || reservas.isEmpty()) {
            return new ArrayList<>();
        }

        // Java Streams
        return reservas.stream()
                // Por cada 'entidad' en la lista, llama a 'mapToDTO'
                .map(this::mapToDTOReserva)
                .collect(Collectors.toList()); // Recolecta en la lista final
    }


    /** Mapea una única Entidad Reserva a un único ReservaDTO.
     */
    public ReservaDTO mapToDTOReserva(Reserva entidad) {
        ReservaDTO dto = new ReservaDTO();

        //dto.setNumeroHab(entidad.getNro_habitacion());
        dto.setNombre(entidad.getNombre());
        dto.setApellido(entidad.getApellido());
        dto.setTipoHab(entidad.getTipo_habitacion());
        dto.setFechaDesde(entidad.getFecha_desde());
        dto.setFechaHasta(entidad.getFecha_hasta());
        dto.setEstado(entidad.getEstado());
        dto.setTelefono(entidad.getTelefono());
        dto.setNumeroHab(entidad.getNumero_habitacion());


        return dto;
    }



}


