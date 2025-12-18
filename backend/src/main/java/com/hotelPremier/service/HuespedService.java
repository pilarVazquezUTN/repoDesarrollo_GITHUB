package com.hotelPremier.service;

import com.hotelPremier.classes.DTO.HuespedDTO;
import com.hotelPremier.classes.Dominio.Direccion;
import com.hotelPremier.classes.Dominio.Huesped;
import com.hotelPremier.classes.Dominio.HuespedID;
import com.hotelPremier.exception.RecursoNoEncontradoException;
import com.hotelPremier.classes.mapper.ClassMapper;
import com.hotelPremier.repository.DireccionRepository;
import com.hotelPremier.repository.HuespedRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HuespedService {

    @Autowired
    private HuespedRepository huespedRepository;

    @Autowired
    private ClassMapper mapper;

    @Autowired
    private DireccionRepository direccionRepository;

    /**
     * Obtiene todos los huéspedes registrados.
     */
    public List<HuespedDTO> findAll() {
        return mapper.toDtos(huespedRepository.findAll());
    }

    /**
     * Busca huéspedes por DNI.
     */
    public List<HuespedDTO> findByCategory(String dni) {
        return mapper.toDtos(
                huespedRepository.findByHuespedID_Dni(dni)
        );
    }

    /**
     * Busca huéspedes por múltiples criterios.
     */
    public List<HuespedDTO> buscarHuespedes(
            String dni,
            String nombre,
            String apellido,
            String tipoDocumento
    ) {
        List<Huesped> lista = huespedRepository.buscarHuespedes(
                dni, nombre, apellido, tipoDocumento
        );
        return mapper.toDtos(lista);
    }

    /**
     * Registra un nuevo huésped junto con su dirección.
     */
    public HuespedDTO addHuesped(HuespedDTO huespedDTO) {

        Huesped huesped = mapper.toEntity(huespedDTO);
        Direccion direccion = huesped.getDireccion();

        direccionRepository.save(direccion);
        Huesped saved = huespedRepository.save(huesped);

        return mapper.toDTO(saved);
    }

    /**
     * Elimina un huésped por tipo y número de documento.
     */
    public void deleteHuesped(String tipoDocumento, String dni) {

        HuespedID id = new HuespedID();
        id.setTipoDocumento(tipoDocumento);
        id.setDni(dni);

        if (!huespedRepository.existsById(id)) {
            throw new RecursoNoEncontradoException(
                String.format("Huésped no encontrado con tipo de documento: %s y DNI: %s", tipoDocumento, dni)
            );
        }

        huespedRepository.deleteById(id);
    }

    /**
     * Actualiza un huésped existente por tipo y número de documento.
     */
    public HuespedDTO updateHuesped(HuespedDTO huespedDTO) {
        // Extraer tipo y dni del DTO
        if (huespedDTO.getHuespedID() == null) {
            throw new IllegalArgumentException("El DTO debe contener el ID del huésped (tipoDocumento y dni)");
        }
        
        String tipoDocumento = huespedDTO.getHuespedID().getTipoDocumento();
        String dni = huespedDTO.getHuespedID().getDni();
        
        if (tipoDocumento == null || dni == null) {
            throw new IllegalArgumentException("El DTO debe contener tipoDocumento y dni en huespedID");
        }
        
        HuespedID id = new HuespedID();
        id.setTipoDocumento(tipoDocumento);
        id.setDni(dni);

        Huesped huespedExistente = huespedRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    String.format("Huésped no encontrado con tipo de documento: %s y DNI: %s", tipoDocumento, dni)
                ));

        // Actualizar los campos del huésped existente con los datos del DTO
        huespedExistente.setNombre(huespedDTO.getNombre());
        huespedExistente.setApellido(huespedDTO.getApellido());
        huespedExistente.setFechaNacimiento(huespedDTO.getFechaNacimiento());
        huespedExistente.setTelefono(huespedDTO.getTelefono());
        huespedExistente.setEmail(huespedDTO.getEmail());
        huespedExistente.setCuit(huespedDTO.getCuit());
        huespedExistente.setPosicionIva(huespedDTO.getPosicionIva());
        huespedExistente.setOcupacion(huespedDTO.getOcupacion());
        huespedExistente.setNacionalidad(huespedDTO.getNacionalidad());

        // Actualizar la dirección si existe
        if (huespedDTO.getDireccion() != null) {
            if (huespedExistente.getDireccion() != null) {
                // Si ya tiene dirección, actualizar sus campos
                Direccion direccionExistente = huespedExistente.getDireccion();
                direccionExistente.setCalle(huespedDTO.getDireccion().getCalle());
                direccionExistente.setNumero(huespedDTO.getDireccion().getNumero());
                direccionExistente.setLocalidad(huespedDTO.getDireccion().getLocalidad());
                direccionExistente.setDepartamento(huespedDTO.getDireccion().getDepartamento());
                direccionExistente.setPiso(huespedDTO.getDireccion().getPiso());
                direccionExistente.setCodigoPostal(huespedDTO.getDireccion().getCodigoPostal());
                direccionExistente.setProvincia(huespedDTO.getDireccion().getProvincia());
                direccionExistente.setPais(huespedDTO.getDireccion().getPais());
                direccionRepository.save(direccionExistente);
            } else {
                // Si no tiene dirección, crear una nueva
                Direccion nuevaDireccion = new Direccion();
                nuevaDireccion.setCalle(huespedDTO.getDireccion().getCalle());
                nuevaDireccion.setNumero(huespedDTO.getDireccion().getNumero());
                nuevaDireccion.setLocalidad(huespedDTO.getDireccion().getLocalidad());
                nuevaDireccion.setDepartamento(huespedDTO.getDireccion().getDepartamento());
                nuevaDireccion.setPiso(huespedDTO.getDireccion().getPiso());
                nuevaDireccion.setCodigoPostal(huespedDTO.getDireccion().getCodigoPostal());
                nuevaDireccion.setProvincia(huespedDTO.getDireccion().getProvincia());
                nuevaDireccion.setPais(huespedDTO.getDireccion().getPais());
                direccionRepository.save(nuevaDireccion);
                huespedExistente.setDireccion(nuevaDireccion);
            }
        }

        Huesped updated = huespedRepository.save(huespedExistente);
        return mapper.toDTO(updated);
    }
}
