package com.olah.clients.rest;

import com.olah.clients.exception.IgrejaCadastradaException;
import com.olah.clients.model.entity.Igreja;
import com.olah.clients.model.repository.IgrejaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/igrejas")
public class IgrejaController {

    @Autowired
    private IgrejaRepository repository;

    @Autowired
    private EntityManager entity;

    @GetMapping
    public List<Igreja> obterTodas() {
        List<Igreja> igrejas = repository.findAll();
        return igrejas;
    }

    @GetMapping("/grupoCongregacional={idGrupo}")
    public List<Igreja> findByGrupoCongregacional(@PathVariable Integer idGrupo) {
        return repository.findByGrupoCongregacionalId(idGrupo);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void salvar(@RequestBody @Valid Igreja igreja) {
        if (repository.existsByNome(igreja.getNome())) {
            throw new IgrejaCadastradaException(igreja.getNome());
        }
        repository.save(igreja);
    }

    @GetMapping("/likeNome={nome}")
    public List<Igreja> obterIgrejasPorNomeLike(@PathVariable String nome) {
        Query query = entity.createQuery("select i from Igreja i where UPPER(i.nome) like UPPER(?1)", Igreja.class);
        query.setParameter(1, nome+"%");
        List<Igreja> igrejas = query.getResultList();
        return igrejas;
    }

}
