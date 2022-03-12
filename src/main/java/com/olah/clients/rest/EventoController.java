package com.olah.clients.rest;

import com.olah.clients.model.entity.Evento;
import com.olah.clients.model.repository.EventoRepository;
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
@RequestMapping("/api/eventos")
public class EventoController {

    @Autowired
    private EventoRepository repository;

    @Autowired
    private EntityManager entity;

    @GetMapping
    public List<Evento> obterTodos() {
        List<Evento> eventos = repository.findAll();
        return eventos;
    }

    @GetMapping("/idIgreja={idIgreja}")
    public Page<Evento> findByIgrejaPaged(@PathVariable Integer idIgreja,
                                                       @RequestParam(value= "page", defaultValue = "0") Integer pagina,
                                                       @RequestParam(value = "size", defaultValue = "10") Integer tamanhoPagina,
                                                       @SortDefault(sort = "dataInicio", direction = Sort.Direction.ASC) Sort sort) {
        Pageable pageable = PageRequest.of(pagina, tamanhoPagina, sort);
        return repository.findByIgrejaId(idIgreja, pageable);
    }

    @GetMapping("{id}")
    public Evento selectPorId(@PathVariable Integer id) {
        return repository.findById(id)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USUARIO_MASTER','USUARIO_ADMINISTRADOR')")
    public void salvar(@RequestBody @Valid Evento evento) {
        repository.save(evento);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('USUARIO_MASTER','USUARIO_ADMINISTRADOR')")
    public void atualizar(@PathVariable Integer id, @RequestBody @Valid Evento eventoAtualizado) {
        repository.findById(id)
                .map( evento -> {
                    eventoAtualizado.setId(id);
                    return repository.save(eventoAtualizado);
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado."));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('USUARIO_MASTER','USUARIO_ADMINISTRADOR')")
    public void deletar( @PathVariable Integer id) {
        repository.findById(id)
                .map(evento -> {
                    repository.delete(evento);
                    return Void.TYPE;
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado"));
    }

}
