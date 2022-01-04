package com.olah.clients.model.repository;

import com.olah.clients.model.entity.UsuarioRedeSocial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRedeSocialRepository extends JpaRepository<UsuarioRedeSocial, Integer>  {

    List<UsuarioRedeSocial> findByEmail(String email);

}
