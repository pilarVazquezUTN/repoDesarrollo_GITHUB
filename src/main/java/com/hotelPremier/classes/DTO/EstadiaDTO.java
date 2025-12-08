package com.hotelPremier.classes.DTO;

import java.util.Date;
import java.util.List;

import com.hotelPremier.classes.Dominio.habitacion.*;
import com.hotelPremier.classes.Dominio.servicioExtra.ServicioExtra;

public class EstadiaDTO {

    private Date checkin; 
    private Date checkout;
    private HabitacionDTO habitacion;
    private ReservaDTO reserva;

    //no estamos seguras!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
    private DobleEstandarDTO dobleestandarDTO;
    private DobleSuperiorDTO doblesuperiorDTO;
    private IndividualEstandarDTO individualestandarDTO;
    private SuiteDTO suiteDTO;
    private SuperiorFamilyPlanDTO superiorfamilyplanDTO;


    private List<HuespedDTO> listahuesped;
    private List<FacturaDTO> listafactura;
    private List<ServicioExtra> listaservicioextra;




    public EstadiaDTO(){}
    public EstadiaDTO(Date checkin, Date checkout, HabitacionDTO hab){
        this.checkin=checkin;
        this.checkout=checkout;
        this.habitacion=hab;
    }
    public HabitacionDTO getHabitacion() {
        return this.habitacion;
    }
    public void setHabitacion(HabitacionDTO habitacion) {
        this.habitacion = habitacion;
    }
    /**
     * set de checkin
     * @param checkin
     */
    public void setCheckin(Date checkin) {
        this.checkin = checkin;
    }

    /**
     * set de checkout
     * @param checkout
     */
    public void setCheckout(Date checkout) {
        this.checkout = checkout;
    }

    /**
     * get de checkin
     * @return
     */

    public Date getCheckin(){
        return this.checkin;
    }

    /**
     * get de checkout
     * @return
     */
    public Date getCheckout(){
        return this.checkout;
    }
}
