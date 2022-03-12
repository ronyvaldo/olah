package com.olah.clients.model.repository;

import com.olah.clients.model.entity.Igreja;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IgrejaRepository extends JpaRepository<Igreja, Integer>  {

    boolean existsByNome(String nome);
    List<Igreja> findByGrupoCongregacionalId(Integer idGrupo);
    Page<Igreja> findByGrupoCongregacionalId(Integer idGrupo, Pageable pageable);
}
