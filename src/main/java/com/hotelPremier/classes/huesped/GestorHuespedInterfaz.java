package com.hotelPremier.classes.huesped;

import com.hotelPremier.classes.direccion.DireccionDTO;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public interface GestorHuespedInterfaz {

    public void setIdEmpleado(String idEmpleado);
    public String getIdEmpleado();
    public Huesped darAltaHuesped();
    public void darBajaHuesped();
    public boolean chequearExisteHuesped(HuespedDTO huespedDTO);
    public void modificarHuespedGestor(HuespedDTO huespedDTO, DireccionDTO d, String rutaArchivo, Map<String, String> campos, Map<String, Predicate<String>> validadores );
    public void modificarDatosHuespedArchivo(Map<String, String> campos, HuespedDTO huespedDTO, String rutaArchivo, DireccionDTO direccionDTO, String tipo , String dni);
    public void registrarHuesped(HuespedDTO huespedDTO);
    public boolean verificarDocumento(HuespedDTO huespedDTO);
    public boolean eliminarHuesped(HuespedDTO huespedDTO);
    public boolean seAlojo (HuespedDTO huespedDTO) ;
    public List<HuespedDTO> buscarDatos(String nombreHuesped, String apellidoHuesped, String tipoDoc, String numDoc);
    public boolean esMayorDeEdad(HuespedDTO huespedDTO);


}
