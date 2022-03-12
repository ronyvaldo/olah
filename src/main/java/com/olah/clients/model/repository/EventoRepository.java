package com.olah.clients.model.repository;

import com.olah.clients.model.entity.Evento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Integer>  {

    Page<Evento> findByIgrejaId(Integer idIgreja, Pageable pageable);
}
