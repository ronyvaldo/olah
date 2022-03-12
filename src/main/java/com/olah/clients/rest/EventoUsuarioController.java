package com.olah.clients.rest;

import com.olah.clients.model.entity.EventoUsuario;
import com.olah.clients.model.repository.EventoUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public void salvar(@RequestBody @Valid EventoUsuario evento) {
        repository.save(evento);
    }
}
