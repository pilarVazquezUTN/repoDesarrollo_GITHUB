package com.hotelPremier.service;

import com.hotelPremier.classes.habitacion.Habitacion;
import com.hotelPremier.classes.habitacion.HabitacionDTO;
import com.hotelPremier.repository.HabitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hotelPremier.classes.mapper.ClassMapper ;

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


    /*public List<HabitacionDTO> obtenerPorTipo(String tipo) {
        List<Habitacion> habitaciones = habitacionRepository.findByTipo(tipo);
        return mapper.toDTOsHabitacion(habitaciones);
    }
*/


}
