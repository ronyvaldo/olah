package com.olah.clients.rest;

import com.olah.clients.exception.TipoDespesaException;
import com.olah.clients.model.entity.TipoDespesa;
import com.olah.clients.model.repository.TipoDespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tiposDespesa")
public class TipoDespesaController {

    @Autowired
    private TipoDespesaRepository repository;

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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USUARIO_MASTER','USUARIO_ADMINISTRADOR')")
    public void salvar(@RequestBody @Valid TipoDespesa tipoDespesa) {
        if (isTipoDespesaExiste(tipoDespesa)) {
            throw new TipoDespesaException(tipoDespesa.getNome());
        }
        repository.save(tipoDespesa);
    }

    private Boolean isTipoDespesaExiste(@PathVariable TipoDespesa tipoDespesa) {
        Boolean retorno = false;
        StringBuilder stringQuery = new StringBuilder()
                .append("SELECT 1 FROM public.tipo_despesa td ")
                .append("   WHERE upper(td.nome) like upper(?1) ");
        if (tipoDespesa.getIgreja() != null && tipoDespesa.getIgreja().getId() > 0) {
            stringQuery.append(" AND (td.id_igreja is null or td.id_igreja=?2)");
        } else {
            stringQuery.append("AND (td.id_grupo_congregacional is null or td.id_grupo_congregacional=?2)");
        }
        Query query = entity.createNativeQuery(stringQuery.toString());
        query.setParameter(1, tipoDespesa.getNome());
        if (tipoDespesa.getIgreja() != null && tipoDespesa.getIgreja().getId() > 0) {
            query.setParameter(2, tipoDespesa.getIgreja().getId());
        } else {
            query.setParameter(2, tipoDespesa.getGrupoCongregacional().getId());
        }
        try {
            Integer result = (Integer) query.getSingleResult();
            if (result != null && result == 1) {
                retorno = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }
}
