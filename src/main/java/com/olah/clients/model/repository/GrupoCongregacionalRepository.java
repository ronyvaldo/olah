package com.olah.clients.model.repository;

import com.olah.clients.model.entity.GrupoCongregacional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrupoCongregacionalRepository extends JpaRepository<GrupoCongregacional, Integer> {

    boolean existsByNome(String nome);
    Page<GrupoCongregacional> findAll(Pageable pageable);
    
}
