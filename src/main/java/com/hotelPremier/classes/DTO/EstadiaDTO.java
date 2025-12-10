package com.hotelPremier.classes.DTO;

import java.util.Date;
import java.util.List;

public class EstadiaDTO {

    private Integer id_estadia;
    private Date checkin;
    private Date checkout;
    private HabitacionDTO habitacion;
    private List<HuespedDTO> listahuesped;
    private List<FacturaDTO> listafactura;
    private ReservaDTO reserva;
    private String estado;

    // ===== GETTERS / SETTERS CORRECTOS =====
    public Integer getId_estadia() {
        return id_estadia;
    }

    public void setId_estadia(Integer id_estadia) {
        this.id_estadia = id_estadia;
    }

    // SI QUERÉS MANTENER getID() → DEJALO COMO ALIAS
    public Integer getID() { 
        return id_estadia; 
    }
    public void setID(Integer id_estadia) { 
        this.id_estadia = id_estadia; 
    }

    public Date getCheckin() { return checkin; }
    public void setCheckin(Date checkin) { this.checkin = checkin; }

    public Date getCheckout() { return checkout; }
    public void setCheckout(Date checkout) { this.checkout = checkout; }

    public HabitacionDTO getHabitacion() { return habitacion; }
    public void setHabitacion(HabitacionDTO habitacion) { this.habitacion = habitacion; }

    public ReservaDTO getReserva() { return reserva; }
    public void setReserva(ReservaDTO reserva) { this.reserva = reserva; }

    public List<HuespedDTO> getListahuesped() { return listahuesped; }
    public void setListahuesped(List<HuespedDTO> listahuesped) { this.listahuesped = listahuesped; }

    public List<FacturaDTO> getListafactura() { return listafactura; }
    public void setListafactura(List<FacturaDTO> listafactura) { this.listafactura = listafactura; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
