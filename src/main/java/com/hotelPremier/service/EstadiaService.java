
package com.hotelPremier.service;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import com.hotelPremier.classes.estadia.Estadia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotelPremier.repository.EstadiaRepository;

import com.hotelPremier.classes.reserva.Reserva;

import com.hotelPremier.classes.estadia.EstadiaDTO;
import com.hotelPremier.classes.habitacion.HabitacionDTO;
import com.hotelPremier.classes.mapper.ClassMapper;

@Service
public class EstadiaService {

    @Autowired
    private EstadiaRepository estadiaRepository;

    @Autowired
    private ClassMapper mapper; 

    @Autowired
    private ReservaService reservaService;


    public void guardarEstadia(EstadiaDTO estadiaDTO){
        Estadia estadia = new Estadia();
        estadia= mapper.toEntity(estadiaDTO);
        estadiaRepository.save(estadia);

        /*
        1. busco la habitacion por el numero (estadiaDTO.getHabitacion()) y me traigo su lista de reservas.
        2. si esta vacia no hago nada.
        2. si interferi con una reserva, la elimino.
         */
        //verificar is la estadia no rompe una reserva. Si la rompe, la elimino.

        Date estadiaFechaDesde = estadiaDTO.getCheckin();
        Date estadiaFechaHasta = estadiaDTO.getCheckout();


        List<Reserva> listareservahabitacion = new ArrayList();
        HabitacionDTO habitacionDTO = new HabitacionDTO();

        habitacionDTO = estadiaDTO.getHabitacion(); //sacamos la habitacion que usa la estadia
        listareservahabitacion = mapper.toEntityReservaLista( habitacionDTO.getListareservas() ); //sacamos la lista de reservas de esa habitacion

        System.out.println("Entramos a elimnar reserva 0");
        System.out.println(listareservahabitacion);
        if( !(listareservahabitacion==null) ){//si existe alguna reserva en la habitacion

            System.out.println("Entramos a elimnar reserva 1");
            for(Reserva r : listareservahabitacion){//recorro todas las reservas de la habitacion

                System.out.println("Entramos a elimnar reserva 2");
                if( reservaService.existeReserva(estadiaFechaDesde,estadiaFechaHasta) ){//si la reserva coincide con la fecha de la nueva estadia
                    System.out.println("Entramos a elimnar reserva 3");
                    System.out.println("nombre "+ r.getNombre());
                    System.out.println("apellido"+r.getApellido());
                    System.out.println("fechades"+r.getFecha_desde());
                    System.out.println("fechaHasta"+r.getFecha_hasta());
                    System.out.println("ID "+r.getId_reserva());
                    System.out.println("Habitacion "+habitacionDTO.getNumero());

                    Integer id = reservaService.buscarId(r,habitacionDTO);
                    System.out.println("ID ENCONTRADO"+id);

                    reservaService.deleteReserva(reservaService.buscarId(r,habitacionDTO));



                    System.out.println("APELLIDO ELIMINADOOOOO"+r.getId_reserva());
                }
            }

        }


        //if(reservaService.existeReserva(estadiaDTO.getHabitacion()))
        //reservaService.eliminarReserva(estadiaDTO.getNumeroHabitacion());

    }

    public void guardarListaEstadia(List<EstadiaDTO> listaestadiaDTO){
        List<Estadia> listaestadia = new ArrayList<>();
        listaestadia = mapper.toEntityEstadia(listaestadiaDTO);

        //por cada estadia, la voy cargando en la bdd.
        //tambien actualizo cada reserva que haya superpuesto.
        for(Estadia e : listaestadia){
            
            estadiaRepository.save(e);//me guardo una estadia en la bdd
        }
    }
    //@Autowired
    //private ClassMapper


    /*public List<EstadiaDTO> getListaEstadias(){
        
        return 
    }*/
}



