package com.hotelPremier.service;

import java.util.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hotelPremier.classes.habitacion.Habitacion;
import com.hotelPremier.classes.habitacion.HabitacionDTO;
import com.hotelPremier.classes.reserva.Reserva;
import com.hotelPremier.repository.HabitacionRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hotelPremier.classes.mapper.ClassMapper;
import com.hotelPremier.repository.ReservaRepository;

import com.hotelPremier.classes.reserva.ReservaDTO;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private HabitacionRepository habitacionRepository;
    @Autowired
    private ClassMapper mapper;

    public List<ReservaDTO> getReservas(
        Date fechaDesde,
        Date fechaHasta
    ){
        return mapper.toDtosReserva( reservaRepository.buscarReservas(fechaDesde,fechaHasta) );
    } 


    public Integer buscarId (Reserva reserva, HabitacionDTO habitacionDTO){
        Integer num_hab=habitacionDTO.getNumero();
        Date fecha_desde=reserva.getFecha_desde();
        Date fecha_hasta=reserva.getFecha_hasta();
        return reservaRepository.encontrarIdReserva(fecha_desde, fecha_hasta, num_hab);
    }

    public void deleteReserva(Integer id){
        reservaRepository.deleteById(id);
    }

    public boolean existeReserva(Date fechaDesde, Date fechaHasta){
        
        return !(reservaRepository.buscarReservas(fechaDesde, fechaHasta).isEmpty());
        //si no esta vacia, existen reservas interferidas.
        //si esta vacia, no hay reservas interferidas.
    }

    /**
     * recibe la lista de reservasdto a cda uno lo mapea a lla entity y lo manda a la bdd
     * @param reservasDTOList
     * @return
     */
    public List<ReservaDTO> crearReserva(List<ReservaDTO> reservasDTOList){

        List<ReservaDTO> reservasGuardadas = new ArrayList<>();

        // loop sobre cada reservaDTO de la lista
        for (ReservaDTO reservaDTO : reservasDTOList) {

           //convertir la entidad
            Reserva reserva = mapper.toEntityReserva(reservaDTO);


            //se busca la hab
            System.out.println("Buscando habitación: " + reservaDTO.getNro_habitacion());



            Habitacion hab = habitacionRepository.findByNumero(reservaDTO.getNro_habitacion());
            System.out.println("habitacion encontrada: " + hab);

            // setear habitación
            reserva.setHabitacion(hab);

            // setear estado
            reserva.setEstado("Pendiente");

            // guardarla en la bdd
            Reserva guardada = reservaRepository.save(reserva);

            // las reservas guardadas
            reservasGuardadas.add(mapper.toDTOReserva(guardada));
        }

        //se devuelve la lista completa
        return reservasGuardadas;
    }

    /**
     * me llega una fecha desde, hasta , una lista de habitacion dto, hace una lista de map para mostrar los datos a guardar dsp en sus respectivas entidades
     * @param fechaDesde
     * @param fechaHasta
     * @return
     */
    public List<Map<String, Object>> generarListadoReservar(
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            List<HabitacionDTO> habitacionDTOS) {

        List<Map<String, Object>> listado = new ArrayList<>();

        for (HabitacionDTO habitacionDTO : habitacionDTOS) {  //por cada habitacionDTO en la lista habitacionDTOS

            Map<String, Object> datosReserva = new HashMap<>();

            datosReserva.put("numero", habitacionDTO.getNumero());
            datosReserva.put("tipo", habitacionDTO.getTipohabitacion());
            datosReserva.put("fechaDesde", fechaDesde);
            datosReserva.put("fechaHasta", fechaHasta);

            listado.add(datosReserva); //este es el listado q devuelve el "gestor" para mostrar por pantalla las seleccionadas
        }

        return listado;
    }





}
