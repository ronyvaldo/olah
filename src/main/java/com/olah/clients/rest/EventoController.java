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
import javax.persistence.Query;
import javax.validation.Valid;
import java.util.ArrayList;
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

    @GetMapping("/idUsuario={idUsuario}")
    public List<Evento> findByIgrejaAndUsuario(@PathVariable Integer idUsuario,
                                               @RequestParam(value= "idIgreja") Integer idIgreja) {
        List<Evento> retorno = new ArrayList<Evento>();
        StringBuilder stringQuery = new StringBuilder()
                .append("SELECT e.id, ")
                .append("   CASE WHEN EXISTS (SELECT 1 FROM public.evento_usuario eu ")
                .append(" WHERE eu.id_evento=e.id and eu.id_usuario=?1) THEN 1 ELSE 0 END AS usuarioInscrito ")
                .append("   FROM public.evento e WHERE e.id_igreja = ?2 ")
                .append(" AND e.data_inicio > CURRENT_DATE ");
        Query query = entity.createNativeQuery(stringQuery.toString());
        query.setParameter(1, idUsuario);
        query.setParameter(2, idIgreja);
        try {
            List<Object[]> result = (List<Object[]>) query.getResultList();
            Evento evento = null;
            for (Object[] l : result) {
                evento = new Evento();
                evento.setId((Integer) l[0]);
                evento = selectPorId(evento.getId());
                evento.setUsuarioInscrito((Integer)l[1] == 1);
                retorno.add(evento);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
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
