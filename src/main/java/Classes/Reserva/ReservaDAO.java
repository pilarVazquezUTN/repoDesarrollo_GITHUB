package Classes.Reserva;

import Classes.ConexionDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO implements ReservaDAOInterfaz {
    private static ReservaDAO instancia; // única instancia

    private ReservaDAO() { }

    /**
     * Devuelve la única instancia de ReservaDAO.
     * Si todavía no fue creada, la instancia se genera y se guarda.
     *
     * @return instancia única de ReservaDAO
     */
    public static synchronized ReservaDAO getInstancia() {
        if (instancia == null) {
            instancia = new ReservaDAO();
        }
        return instancia;
    }
    
    /**
     * Elimina un elemento de la base de datos.
     */
    public void delete(){

    }
    /**
     * Crea un nuevo elemento de la base de datos.
     */
    public  void create(){
    }
    /**
     * Actualiza un elemento de la base de datos.
     */
    public  void update(){
    }
    /**
     * Lee un elemento de la base de datos, SI apellido es null , lee todos, sino lee por ese apellido
     */
    public List<ReservaDTO> read(String apellido ){ //HAGO EL METODO QUE LEE TODAS LAS RESERVAS SI APELLIDO ES != NULL , BUSCA POR ESE APELLIDO

        List<ReservaDTO> listaReservas = new ArrayList<>();


        final String SQL = "SELECT "
                + "r.apellido, r.nombre, r.nro_habitacion, r.fecha_desde, r.fecha_hasta, " // Campos de Reserva
                + "hab.tipohabitacion " // Campo de Habitación
                + "FROM Reserva r "
                + "JOIN habitacion hab ON hab.nro_habitacion = r.nro_habitacion "
                + "WHERE r.apellido ILIKE ?"; // El WHERE va al final


        try (Connection conn = ConexionDataBase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            // 1. Asignar el parámetro
            stmt.setString(1, "%" + apellido + "%"); //el 1 dice q es el primer signo interrogacion , se reemplaza por apellido

            //rs es el result set de la query
            try (ResultSet rs = stmt.executeQuery()) {
                // 2. Iterar y mapear a DTO
                while (rs.next()) {
                    //instancia del dto
                    ReservaDTO reservadto = new ReservaDTO();

                    // Mapeo de BDD (ResultSet) a DTO

                    reservadto.setFechaDesde(rs.getDate("fecha_desde"));

                    reservadto.setFechaHasta(rs.getDate("fecha_hasta"));
                    reservadto.setApellido(rs.getString("apellido"));

                    reservadto.setNombre(rs.getString("nombre"));

                    reservadto.setNumeroHab(rs.getInt("nro_habitacion"));
                    reservadto.setTipoHab(rs.getString("tipohabitacion"));

                    listaReservas.add(reservadto);
                }
            }
        } catch (SQLException e) {
            // errores
        }
        return listaReservas; // Devuelve la lista de DTOs
    }

    }



