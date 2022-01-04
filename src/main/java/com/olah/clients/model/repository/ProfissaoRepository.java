package com.olah.clients.model.repository;

import com.olah.clients.model.entity.Profissao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfissaoRepository extends JpaRepository<Profissao, Integer> {
}
