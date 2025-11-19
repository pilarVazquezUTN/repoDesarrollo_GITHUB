package com.hotelPremier.repository;

import java.util.*;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import Classes.Usuario.Usuario;
import Classes.Usuario.UsuarioDTO;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario,Long>{
    @Override
    List<Usuario> findAll();
    List<Usuario> findByCategory(String category);
    List<Usuario> buscarUsuario(UsuarioDTO usuarioDTO);
    //Set<Usuario> usuarioEncontrado();
}
