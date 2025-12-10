package com.hotelPremier.service;

import com.hotelPremier.classes.DTO.EstadiaDTO;
import com.hotelPremier.classes.DTO.HabitacionDTO;
import com.hotelPremier.classes.DTO.ReservaDTO;
import com.hotelPremier.classes.Dominio.Estadia;
import com.hotelPremier.classes.Dominio.Habitacion;
import com.hotelPremier.classes.Dominio.Huesped;
import com.hotelPremier.classes.Dominio.Reserva;
import com.hotelPremier.repository.HabitacionRepository;

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

    //llama al repo para la bdd y devuelve el dto
    public List<HabitacionDTO> getHabitaciones(String tipoHabitacion) {
        return mapper.toDTOsHabitacion( habitacionRepository.buscarPorTipoHabitacion(tipoHabitacion) );
    }

    public HabitacionDTO getHabitacion(Integer numero){
        return mapper.toDTOHab( habitacionRepository.findByNumero(numero) );
    }

    public List<HabitacionDTO> buscarListaHabitaciones(String tipo, Date fechaDesde, Date fechaHasta){ 
        return mapper.toDTOsHabitacion( habitacionRepository.buscarListaHabitaciones(tipo,fechaDesde,fechaHasta) );
    }


    public List<HabitacionDTO> obtenerHabitacionesConDetalle(String tipo, Date fechaDesde, Date fechaHasta) {
//trae una list de habitaciones de la bdd
        List<Habitacion> habitaciones = habitacionRepository.buscarPorTipoHabitacion(tipo);
        List<HabitacionDTO> resultado = new ArrayList<>();

//por cada hab de la q trajo de la bdd
        for (Habitacion h : habitaciones) {

            // Hibernate carga las listas automáticamente con LAZY o EAGER
            //agarra las listas de cada hab
            List<Reserva> reservas = h.getListareservas()
                .stream()//convertimos la lista a stream, para procesarlos mejor
                .filter(r ->
                    r.getFecha_desde().compareTo(fechaHasta) <= 0 &&
                    r.getFecha_hasta().compareTo(fechaDesde) >= 0
                )
                //.map(reserva -> mapper.toDTOReserva(reserva))
                .toList();

            // ESTADÍAS FILTRADAS
            //List<E<stadia> estadias = h.getListaEstadia();
            List<Estadia> estadias = h.getListaEstadia()
                .stream()
                .map(e -> (Estadia) e) 
                .filter(e ->
                    e.getCheckin().compareTo(fechaHasta) <= 0 &&
                    e.getCheckout().compareTo(fechaDesde) >= 0
                )
                //.map(estadia -> mapper.toDTOEstadia(estadia))
                .toList();


            List<ReservaDTO> reservasDTO = new ArrayList<>();
            reservasDTO=mapper.toDtosReserva(reservas);

            List<EstadiaDTO> estadiasDTO = new ArrayList<>();
            estadiasDTO=mapper.toDTOsEstadia(estadias);
            //List<Reserva> reservas = reservaService.getReservas(desdeFecha,hastaFecha);

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

    
     public List<HabitacionDTO> getHabitaciones() {
        return mapper.toDTOsHabitacion(habitacionRepository.findAll());
    }
}
