package com.olah.clients.model.repository;

import com.olah.clients.model.entity.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DespesaRepository extends JpaRepository<Despesa, Integer>  {
}
