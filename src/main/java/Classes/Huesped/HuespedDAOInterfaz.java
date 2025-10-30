package Classes.Huesped;

import Classes.Direccion.DireccionDTO;
import Classes.Excepciones.HuespedNoEncontradoException;
import Classes.Huesped.HuespedDTO;

import java.util.List;
import java.util.Map;

public interface HuespedDAOInterfaz {
    void delete();
    void create();
    void update();
    void read();

    boolean verificarDocumento(HuespedDTO huespedDTO);

    void registrarHuesped(HuespedDTO huespedDTO);

    List<HuespedDTO> buscarDatos(String nombreHuesped, String apellidoHuesped, String tipoDoc, String numDoc) throws HuespedNoEncontradoException;

    boolean actualizarHuesped(Map<String, String> campos, String rutaArchivo, HuespedDTO huespedDTO, DireccionDTO direccionDTO, String tipoDoc, String dni);

    boolean eliminarHuespued(HuespedDTO huesped);

    boolean existeHuesped(HuespedDTO huespedDTO);

    boolean buscarHuespedyReemplazar(String tipodoc, String tipoDoc);

    boolean seAlojo(HuespedDTO huespedDTO);
}
