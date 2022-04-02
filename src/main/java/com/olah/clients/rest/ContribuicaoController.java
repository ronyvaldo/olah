package com.olah.clients.rest;

import com.olah.clients.model.repository.ContribuicaoRepository;
import com.olah.clients.model.entity.Contribuicao;
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

import javax.validation.Valid;

@RestController
@RequestMapping("/api/contribuicoes")
public class ContribuicaoController {

    @Autowired
    private ContribuicaoRepository repository;

    @GetMapping
    @PreAuthorize("hasAnyRole('USUARIO_MASTER','USUARIO_ADMINISTRADOR')")
    public Page<Contribuicao> obterTodas(@RequestParam(value= "page", defaultValue = "0") Integer pagina,
                                         @RequestParam(value = "size", defaultValue = "10") Integer tamanhoPagina,
                                         @SortDefault(sort = "dataCadastro", direction = Sort.Direction.DESC) Sort sort) {
        Pageable pageable = PageRequest.of(pagina, tamanhoPagina, sort);
        return repository.findAll(pageable);
    }

    @GetMapping("{id}")
    public Contribuicao selectPorId(@PathVariable Integer id) {
        return repository.findById(id)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Integer id, @RequestBody @Valid Contribuicao contribuicaoAtualizada) {
        repository.findById(id)
                .map( contribuicao -> {
                    contribuicaoAtualizada.setId(id);
                    return repository.save(contribuicaoAtualizada);
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contribuicão não encontrada."));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USUARIO_MASTER','USUARIO_ADMINISTRADOR')")
    public void salvar(@RequestBody @Valid Contribuicao contribuicao) {
        repository.save(contribuicao);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('USUARIO_MASTER','USUARIO_ADMINISTRADOR')")
    public void deletar( @PathVariable Integer id) {
        repository.findById(id)
                .map(contribuicao -> {
                    repository.delete(contribuicao);
                    return Void.TYPE;
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contribuição não encontrada"));
    }

}
