package Classes.Huesped;

import Classes.Direccion.DireccionDTO;
import Classes.Huesped.HuespedDTO;

import java.util.Map;

public interface HuespedDAOInterfaz {
    void delete();
    void create();
    void update();
    void read();

    /**
     *
     * @param huespedDTO
     * @return
     */
    boolean verificarDocumento(HuespedDTO huespedDTO);

    /**
     *
     * @param huespedDTO
     */
    void registrarHuesped(HuespedDTO huespedDTO);

    /**
     *
     * @param nombreHuesped
     * @param apellidoHuesped
     * @param tipoDoc
     * @param numDoc
     * @return
     */
    HuespedDTO buscarDatos(String nombreHuesped, String apellidoHuesped, String tipoDoc, String numDoc);

    /**
     *
     * @param campos
     * @param rutaArchivo
     * @param huespedDTO
     * @param direccionDTO
     * @param tipoDoc
     * @param dni
     * @return
     */
    boolean actualizarHuesped(Map<String, String> campos, String rutaArchivo, HuespedDTO huespedDTO, DireccionDTO direccionDTO, String tipoDoc, String dni);

    /**
     *
     * @param huesped
     * @return
     */
    boolean eliminarHuespued(HuespedDTO huesped);

    /**
     *
     * @param huespedDTO
     * @return
     */
    boolean existeHuesped(HuespedDTO huespedDTO);

    /**
     *
     * @param tipodoc
     * @param tipoDoc
     * @return
     */
    boolean buscarHuespedyReemplazar(String tipodoc, String tipoDoc);

    /**
     *
     * @param huespedDTO
     * @return
     */
    boolean seAlojo(HuespedDTO huespedDTO);
}
