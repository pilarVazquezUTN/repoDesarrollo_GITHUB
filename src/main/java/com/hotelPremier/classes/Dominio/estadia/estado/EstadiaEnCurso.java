package com.hotelPremier.classes.Dominio.estadia.estado;

import com.hotelPremier.classes.Dominio.Estadia;
import com.hotelPremier.classes.Dominio.Huesped;
import com.hotelPremier.classes.Dominio.servicioExtra.ServicioExtra;
import com.hotelPremier.classes.Dominio.Factura;
import java.util.List;

/**
 * Estado ENCURSO de una estadía.
 * El huésped está alojado.
 * 
 * Permite:
 * - agregar huéspedes
 * - agregar servicios extra
 * - generar facturas
 * - consultar estado de la habitación
 * 
 * No permite:
 * - modificar fechas
 * - volver atrás
 */
public class EstadiaEnCurso extends EstadoEstadia {

    @Override
    public String getNombre() {
        return "ENCURSO";
    }

    @Override
    public void agregarHuesped(Estadia estadia, Huesped huesped) {
        List<Huesped> lista = estadia.getListahuesped();
        if (lista != null && !lista.contains(huesped)) {
            lista.add(huesped);
        }
        // La relación ManyToMany se maneja automáticamente por JPA
    }

    @Override
    public void agregarServicioExtra(Estadia estadia, ServicioExtra servicio) {
        // La carga de servicios extra se maneja desde el service.
        // Este método valida que la operación sea permitida en este estado.
        // La implementación concreta (persistencia, asociaciones) se realiza en el service.
        // No hacer nada aquí evita bugs silenciosos: el service debe manejar la lógica completa.
    }

    @Override
    public void generarFactura(Estadia estadia, Factura factura) {
        List<Factura> lista = estadia.getListafactura();
        if (lista != null) {
            factura.setEstadia(estadia);
            lista.add(factura);
        }
    }

    @Override
    public void finalizar(Estadia estadia) {
        estadia.setEstadoEstadia(new EstadiaFinalizada());
    }
}

