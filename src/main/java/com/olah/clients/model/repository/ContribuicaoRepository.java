package com.olah.clients.model.repository;

import com.olah.clients.model.entity.Contribuicao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContribuicaoRepository extends JpaRepository<Contribuicao, Integer>  {
}
