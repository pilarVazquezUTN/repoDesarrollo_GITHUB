package com.hotelPremier.repository;

import java.util.*;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hotelPremier.classes.usuario.Usuario;
import com.hotelPremier.classes.usuario.UsuarioDTO;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario,Long>{
    @Override
    List<Usuario> findAll();
    List<Usuario> findBynombre(String nombre);
   // List<Usuario> buscarUsuario(UsuarioDTO usuarioDTO);
    //Set<Usuario> usuarioEncontrado();
}
