package com.olah.clients.rest;

import com.olah.clients.model.entity.EventoUsuario;
import com.olah.clients.model.repository.EventoUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/eventoUsuario")
public class EventoUsuarioController {

    @Autowired
    EventoUsuarioRepository repository;

    @Autowired
    private EntityManager entity;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void salvar(@RequestBody @Valid EventoUsuario eventoUsuario) {
        EventoUsuario retornoConsulta = select(eventoUsuario);
        if (retornoConsulta != null && retornoConsulta.getId() > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Você já está inscrito no evento " +  retornoConsulta.getEvento().getNome() + ".");
        } else {
            repository.save(eventoUsuario);
        }
    }

    public EventoUsuario select(EventoUsuario eventoUsuario) {
        return repository.findByUsuarioIdAndEventoId(
                eventoUsuario.getUsuario().getId(), eventoUsuario.getEvento().getId());
    }
}
