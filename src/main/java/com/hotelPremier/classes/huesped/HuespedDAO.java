package com.hotelPremier.classes.huesped;

import com.hotelPremier.classes.direccion.DireccionDTO;
import com.hotelPremier.classes.excepciones.HuespedNoEncontradoException;

import java.util.List;
import java.util.Map;

/*
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;
import java.text.SimpleDateFormat;

import com.hotelPremier.classes.direccion.DireccionDTO;
import com.hotelPremier.classes.excepciones.HuespedNoEncontradoException;
import com.hotelPremier.classes.FuncionesUtiles;
import com.hotelPremier.classes.huesped.GestorHuesped;

import static java.lang.Class.forName;
*/
public class HuespedDAO implements HuespedDAOInterfaz {
    private static HuespedDAO instancia; // única instancia

    private HuespedDAO() { }

    @Override
    public void delete() {

    }

    @Override
    public void create() {

    }

    @Override
    public void update() {

    }

    @Override
    public void read() {

    }

    @Override
    public boolean verificarDocumento(HuespedDTO huespedDTO) {
        return false;
    }

    @Override
    public void registrarHuesped(HuespedDTO huespedDTO) {

    }

    @Override
    public List<HuespedDTO> buscarDatos(String nombreHuesped, String apellidoHuesped, String tipoDoc, String numDoc) throws HuespedNoEncontradoException {
        return List.of();
    }

    @Override
    public boolean actualizarHuesped(Map<String, String> campos, String rutaArchivo, HuespedDTO huespedDTO, DireccionDTO direccionDTO, String tipoDoc, String dni) {
        return false;
    }

    @Override
    public boolean eliminarHuespued(HuespedDTO huesped) {
        return false;
    }
}


