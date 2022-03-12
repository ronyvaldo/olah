package com.olah.clients.rest;

import com.olah.clients.exception.TipoContribuicaoException;
import com.olah.clients.model.entity.TipoContribuicao;
import com.olah.clients.model.repository.TipoContribuicaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tiposContribuicao")
public class TipoContribuicaoController {

    @Autowired
    private TipoContribuicaoRepository repository;

    @Autowired
    private EntityManager entity;

    @GetMapping("/idIgreja={idIgreja}")
    @PreAuthorize("hasAnyRole('USUARIO_MASTER','USUARIO_ADMINISTRADOR')")
    public List<TipoContribuicao> obterTodosDaIgreja(@PathVariable Integer idIgreja) {
        Query query = entity.createQuery("select t from TipoContribuicao t where (t.igreja.id =?1 or t.igreja.id is null)", TipoContribuicao.class);
        query.setParameter(1, idIgreja);
        List<TipoContribuicao> tiposContribuicao = query.getResultList();
        return tiposContribuicao;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USUARIO_MASTER','USUARIO_ADMINISTRADOR')")
    public void salvar(@RequestBody @Valid TipoContribuicao tipoContribuicao) {
        if (isTipoContribuicaoExiste(tipoContribuicao)) {
            throw new TipoContribuicaoException(tipoContribuicao.getNome());
        }
        repository.save(tipoContribuicao);
    }

    private Boolean isTipoContribuicaoExiste(@PathVariable TipoContribuicao tipoContribuicao) {
        Boolean retorno = false;
        StringBuilder stringQuery = new StringBuilder()
                .append("SELECT 1 FROM public.tipo_contribuicao tc ")
                .append("   WHERE upper(tc.nome) like upper(?1) ");
        if (tipoContribuicao.getIgreja() != null && tipoContribuicao.getIgreja().getId() > 0) {
            stringQuery.append(" AND (tc.id_igreja is null or tc.id_igreja=?2)");
        } else {
            stringQuery.append("AND (tc.id_grupo_congregacional is null or tc.id_grupo_congregacional=?2)");
        }
        Query query = entity.createNativeQuery(stringQuery.toString());
        query.setParameter(1, tipoContribuicao.getNome());
        if (tipoContribuicao.getIgreja() != null && tipoContribuicao.getIgreja().getId() > 0) {
            query.setParameter(2, tipoContribuicao.getIgreja().getId());
        } else {
            query.setParameter(2, tipoContribuicao.getGrupoCongregacional().getId());
        }
        try {
            Object[] result = (Object[]) query.getSingleResult();
            Integer i = ((Integer) result[0]);
            if (i != null && i.equals(1)) {
                retorno = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }
}
