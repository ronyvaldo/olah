package com.olah.clients.rest;

import com.olah.clients.exception.GrupoCongregacionalCadastradoException;
import com.olah.clients.model.entity.GrupoCongregacional;
import com.olah.clients.model.repository.GrupoCongregacionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/grupos")
public class GrupoCongregacionalController {

    @Autowired
    private GrupoCongregacionalRepository repository;

    @Autowired
    private EntityManager entity;

    @GetMapping
    public Page<GrupoCongregacional> obterTodos(@RequestParam(value= "page", defaultValue = "0") Integer pagina,
                                                       @RequestParam(value = "size", defaultValue = "10") Integer tamanhoPagina,
                                                       @SortDefault(sort = "dataCadastro", direction = Sort.Direction.DESC) Sort sort) {
        Pageable pageable = PageRequest.of(pagina, tamanhoPagina, sort);
        return repository.findAll(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USUARIO_MASTER')")
    public void salvar(@RequestBody @Valid GrupoCongregacional grupoCongregacional) {
        if (repository.existsByNome(grupoCongregacional.getNome())) {
            throw new GrupoCongregacionalCadastradoException(grupoCongregacional.getNome());
        }
        repository.save(grupoCongregacional);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('USUARIO_MASTER')")
    public void atualizar(@PathVariable Integer id, @RequestBody @Valid GrupoCongregacional grupoCongregacionalAtualizado) {
        repository.findById(id)
                .map( grupoCongregacional -> {
                    grupoCongregacionalAtualizado.setId(id);
                    return repository.save(grupoCongregacionalAtualizado);
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grupo Congregacional não encontrado."));
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('USUARIO_MASTER','USUARIO_ADMINISTRADOR')")
    public GrupoCongregacional selectPorId(@PathVariable Integer id) {
        return repository.findById(id)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}