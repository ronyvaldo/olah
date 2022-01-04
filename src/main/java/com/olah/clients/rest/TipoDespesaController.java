package com.olah.clients.rest;

import com.olah.clients.model.entity.TipoDespesa;
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
@RequestMapping("/api/tiposDespesa")
public class TipoDespesaController {

    @Autowired
    private EntityManager entity;

    @GetMapping("/idIgreja={idIgreja}")
    @PreAuthorize("hasAnyRole('USUARIO_MASTER','USUARIO_ADMINISTRADOR')")
    public List<TipoDespesa> obterTodosDaIgreja(@PathVariable Integer idIgreja) {
        Query query = entity.createQuery("select t from TipoDespesa t where (t.igreja.id =?1 or t.igreja.id is null)", TipoDespesa.class);
        query.setParameter(1, idIgreja);
        List<TipoDespesa> tiposDespesa = query.getResultList();
        return tiposDespesa;
    }
}
