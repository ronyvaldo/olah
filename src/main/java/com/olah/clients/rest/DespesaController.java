package com.olah.clients.rest;

import com.olah.clients.model.entity.Despesa;
import com.olah.clients.model.repository.DespesaRepository;
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

@RestController
@RequestMapping("/api/despesas")
public class DespesaController {

    @Autowired
    private DespesaRepository repository;

    @Autowired
    private EntityManager entity;

    @GetMapping
    @PreAuthorize("hasAnyRole('USUARIO_MASTER','USUARIO_ADMINISTRADOR')")
    public Page<Despesa> obterTodas(@RequestParam(value= "page", defaultValue = "0") Integer pagina,
                                    @RequestParam(value = "size", defaultValue = "10") Integer tamanhoPagina,
                                    @SortDefault(sort = "dataCadastro", direction = Sort.Direction.DESC) Sort sort) {
        Pageable pageable = PageRequest.of(pagina, tamanhoPagina, sort);
        return repository.findAll(pageable);
    }

    @GetMapping("{id}")
    public Despesa selectPorId(@PathVariable Integer id) {
        return repository.findById(id)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Integer id, @RequestBody @Valid Despesa despesaAtualizada) {
        repository.findById(id)
                .map( despesa -> {
                    despesaAtualizada.setId(id);
                    return repository.save(despesaAtualizada);
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Despesa não encontrada."));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USUARIO_MASTER','USUARIO_ADMINISTRADOR')")
    public void salvar(@RequestBody @Valid Despesa despesa) {
        repository.save(despesa);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('USUARIO_MASTER','USUARIO_ADMINISTRADOR')")
    public void deletar( @PathVariable Integer id) {
        repository.findById(id)
                .map(despesa -> {
                    repository.delete(despesa);
                    return Void.TYPE;
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Despesa não encontrada"));
    }


}
