package com.olah.clients.rest;

import com.olah.clients.model.entity.Profissao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@RestController
@RequestMapping("/api/profissoes")
public class ProfissaoController {

    @Autowired
    private EntityManager entity;

    @GetMapping("/likeNome={nome}")
    @PreAuthorize("hasAnyRole('USUARIO_MASTER','USUARIO_ADMINISTRADOR')")
    public List<Profissao> obterProfissoesPorNomeLike(@PathVariable String nome) {
        Query query = entity.createQuery("select p from Profissao p where UPPER(p.nome) like UPPER(?1)", Profissao.class);
        query.setParameter(1, nome+"%");
        List<Profissao> profissoes = query.getResultList();
        return profissoes;
    }
}
