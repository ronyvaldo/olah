package com.olah.clients.rest;

import com.olah.clients.exception.IgrejaCadastradaException;
import com.olah.clients.model.entity.Igreja;
import com.olah.clients.model.repository.IgrejaRepository;
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

    @GetMapping("/paginado")
    public Page<Igreja> obterTodasPaged(@RequestParam(value= "page", defaultValue = "0") Integer pagina,
                                        @RequestParam(value = "size", defaultValue = "10") Integer tamanhoPagina) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanhoPagina);
        return repository.findAll(pageRequest);
    }

    @GetMapping("/grupoCongregacional={idGrupo}")
    public List<Igreja> findByGrupoCongregacional(@PathVariable Integer idGrupo) {
        return repository.findByGrupoCongregacionalId(idGrupo);
    }

    @GetMapping("/grupoCongregacionalPaged={idGrupo}")
    public Page<Igreja> findByGrupoCongregacionalPaged(@PathVariable Integer idGrupo,
                                                       @RequestParam(value= "page", defaultValue = "0") Integer pagina,
                                                       @RequestParam(value = "size", defaultValue = "10") Integer tamanhoPagina,
                                                       @SortDefault(sort = "dataCadastro", direction = Sort.Direction.DESC) Sort sort) {
        Pageable pageable = PageRequest.of(pagina, tamanhoPagina, sort);
        return repository.findByGrupoCongregacionalId(idGrupo, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USUARIO_MASTER','USUARIO_ADMINISTRADOR')")
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

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('USUARIO_MASTER','USUARIO_ADMINISTRADOR')")
    public void atualizar(@PathVariable Integer id, @RequestBody @Valid Igreja igrejaAtualizada) {
        repository.findById(id)
                .map( igreja -> {
                    igrejaAtualizada.setId(id);
                    return repository.save(igrejaAtualizada);
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Igreja não encontrada."));
    }

    @GetMapping("{id}")
    public Igreja selectPorId(@PathVariable Integer id) {
        return repository.findById(id)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('USUARIO_MASTER','USUARIO_ADMINISTRADOR')")
    public void deletar( @PathVariable Integer id) {
        repository.findById(id)
                .map(igreja -> {
                    repository.delete(igreja);
                    return Void.TYPE;
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Igreja não encontrada"));
    }

}
