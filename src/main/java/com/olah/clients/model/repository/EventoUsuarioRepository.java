package com.olah.clients.model.repository;

import com.olah.clients.model.entity.EventoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoUsuarioRepository extends JpaRepository<EventoUsuario, Integer>  {
}
