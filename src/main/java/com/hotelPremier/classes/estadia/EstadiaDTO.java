package com.hotelPremier.classes.estadia;

import java.util.Date;
import java.util.List;

import com.hotelPremier.classes.habitacion.*;

import com.hotelPremier.classes.servicioExtra.ServicioExtra;
import com.hotelPremier.classes.factura.FacturaDTO;
import com.hotelPremier.classes.huesped.HuespedDTO;

public class EstadiaDTO {
    private Integer id_estadiaDTO;
    private Date checkin; 
    private Date checkout;
    private DobleEstandarDTO dobleestandarDTO;
    private DobleSuperiorDTO doblesuperiorDTO;
    private IndividualEstandarDTO individualestandarDTO;
    private SuiteDTO suiteDTO;
    private SuperiorFamilyPlanDTO superiorfamilyplanDTO;
    private List<HuespedDTO> listahuesped;
    private List<FacturaDTO> listafactura;
    private List<ServicioExtra> listaservicioextra;



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

    public Date getCheckIn(){
        return this.checkin;
    }

    /**
     * get de checkout
     * @return
     */
    public Date getCheckOut(){
        return this.checkout;
    }
}
