package Classes.Habitacion;

import java.util.ArrayList;

import Classes.Reserva.Reserva;

public abstract class Habitacion  {
    private int numero;
    private String estado;
    private float precio;
    private int cantidadPersonas;
    private ArrayList<Reserva> listaReservas= new ArrayList<>();
}
