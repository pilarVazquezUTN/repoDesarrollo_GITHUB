package com.hotelPremier.service;

import com.hotelPremier.classes.estadia.Estadia;
import com.hotelPremier.classes.estadia.EstadiaDTO;
import com.hotelPremier.classes.habitacion.Habitacion;
import com.hotelPremier.classes.habitacion.HabitacionDTO;
import com.hotelPremier.classes.habitacion.HabitacionEstadosDTO;
import com.hotelPremier.classes.huesped.Huesped;
import com.hotelPremier.classes.reserva.ReservaDTO;
import com.hotelPremier.repository.HabitacionRepository;
import com.hotelPremier.service.ReservaService;
import com.hotelPremier.service.EstadiaService;
import com.hotelPremier.classes.reserva.Reserva;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hotelPremier.classes.mapper.ClassMapper ;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class HabitacionService {

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private ClassMapper mapper;

    @Autowired
    private EstadiaService estadiaService;

    @Autowired
    private ReservaService reservaService;


    //llama al repo para la bdd y devuelve el dto
    public List<HabitacionDTO> getHabitaciones(String tipoHabitacion) {
        return mapper.toDTOsHabitacion( habitacionRepository.buscarPorTipoHabitacion(tipoHabitacion) );
    }

    public List<HabitacionDTO> buscarListaHabitaciones(String tipo, Date fechaDesde, Date fechaHasta){ 
        return mapper.toDTOsHabitacion( habitacionRepository.buscarListaHabitaciones(tipo,fechaDesde,fechaHasta) );
    }


    public List<HabitacionDTO> obtenerHabitacionesConDetalle() {
//trae una list de habitaciones de la bdd
        List<Habitacion> habitaciones = habitacionRepository.findAll();
        List<HabitacionDTO> resultado = new ArrayList<>();
//por cada hab de la q trajo de la bdd
        for (Habitacion h : habitaciones) {

            // Hibernate carga las listas automáticamente con LAZY o EAGER
            //agarra las listas de cada hab
            List<Reserva> reservas = h.getListaReservas();
            List<Estadia> estadias = h.getListaEstadia();

            List<ReservaDTO> reservasDTO = new ArrayList<>();
            reservasDTO=mapper.toDtosReserva(reservas);

            List<EstadiaDTO> estadiasDTO = new ArrayList<>();
            estadiasDTO=mapper.toDTOsEstadia(estadias);
            //List<Reserva> reservas = reservaService.getReservas(desdeFecha,hastaFecha);
//las mapea a dto y se las manda a habitacion dto , la hab mas las listas
            resultado.add(new HabitacionDTO(h, reservasDTO, estadiasDTO));
        }

        return resultado;
    }



    /*public List<HabitacionDTO> obtenerPorTipo(String tipo) {
        List<Habitacion> habitaciones = habitacionRepository.findByTipo(tipo);
        return mapper.toDTOsHabitacion(habitaciones);
    }
*/
    public List<Huesped> obtenerHuespedesPorHabitacion(Integer nroHabitacion) {

        Habitacion habitacion = habitacionRepository.findById(nroHabitacion)
                .orElse(null);

        if (habitacion == null) {
            // si no existe, devuelvo lista vacía
            return List.of();
        }
        

        List<Estadia> estadias = habitacion.getListaEstadia();
        if (estadias == null) {
            return List.of();
        }

        return estadias.stream()
                .filter(e -> e.getListahuesped() != null) // evitar null
                .flatMap(e -> e.getListahuesped().stream())
                .distinct()
                .toList();
    }

}
