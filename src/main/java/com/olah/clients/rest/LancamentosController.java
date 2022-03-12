package com.olah.clients.rest;

import com.olah.clients.model.dto.LancamentosDTO;
import com.olah.clients.model.dto.LancamentosIndicadoresDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentosController {

    @Autowired
    private EntityManager entity;

    @GetMapping("/porIgreja={idIgreja}")
    @PreAuthorize("hasAnyRole('USUARIO_MASTER','USUARIO_ADMINISTRADOR')")
    public LancamentosDTO obterLancamentosPorIgreja(@PathVariable Integer idIgreja) {
        StringBuilder stringQuery = new StringBuilder()
                .append("SELECT totalContribuicoes, totalDespesas FROM (SELECT sum(c.valor) totalContribuicoes FROM public.contribuicao c ")
                .append("   WHERE c.id_igreja=?1 and EXTRACT(MONTH FROM c.data_contribuicao) = EXTRACT(MONTH FROM current_timestamp)) a, ")
                .append("(SELECT sum(d.valor) totalDespesas FROM public.despesa d ")
                .append("   WHERE d.id_igreja=?2 and EXTRACT(MONTH FROM d.data_despesa) = EXTRACT(MONTH FROM current_timestamp)) b");
        Query query = entity.createNativeQuery(stringQuery.toString());
        query.setParameter(1, idIgreja);
        query.setParameter(2, idIgreja);
        LancamentosDTO lancamentos = new LancamentosDTO();
        try {
            Object[] result = (Object[]) query.getSingleResult();
            lancamentos.setTotalContribuicoes((Double)result[0]);
            lancamentos.setTotalDespesas((Double)result[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder stringQuery2 = new StringBuilder()
                .append("SELECT totalDeMembros, totalEntregaramContribuicoes FROM (SELECT count(*) as totalDeMembros ")
                .append("   FROM public.usuario u, public.igreja_usuario ui")
                .append(" WHERE ui.id_usuario=u.id")
                .append(" AND u.perfil = 1 AND ui.id_igreja = ?1) a, ")
                .append("(SELECT count(distinct u.id) as totalEntregaramContribuicoes  ")
                .append("   FROM public.usuario u,public.igreja_usuario ui")
                .append(" WHERE ui.id_usuario=u.id ")
                .append(" AND ui.id_igreja = ?2")
                .append(" AND u.id in (SELECT c.id_usuario FROM public.contribuicao c")
                .append("               WHERE EXTRACT(MONTH FROM c.data_contribuicao) = EXTRACT(MONTH FROM current_timestamp))) b");
        Query query2 = entity.createNativeQuery(stringQuery2.toString());
        query2.setParameter(1, idIgreja);
        query2.setParameter(2, idIgreja);
        try {
            Object[] result2 = (Object[]) query2.getSingleResult();
            lancamentos.setTotalDeMembros((BigInteger)result2[0]);
            lancamentos.setTotalEntregaramContribuicoes((BigInteger)result2[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lancamentos;
    }

    @GetMapping("/indicadoresDespesas={idIgreja}")
    @PreAuthorize("hasAnyRole('USUARIO_MASTER','USUARIO_ADMINISTRADOR')")
    public List<LancamentosIndicadoresDTO> obterIndicadoresDespesasPorIgreja(@PathVariable Integer idIgreja) {
        List<LancamentosIndicadoresDTO> retorno = new ArrayList<LancamentosIndicadoresDTO>();
        StringBuilder stringQuery = new StringBuilder()
                .append("SELECT (SELECT t.nome FROM public.tipo_despesa t ")
                .append("   WHERE t.id=d.id_tipo_despesa) tipo_despesa,sum(d.valor) FROM public.despesa d ")
                .append(" WHERE d.id_igreja=?1 ")
                .append("   AND EXTRACT(MONTH FROM d.data_despesa) = EXTRACT(MONTH FROM current_timestamp) ")
                .append(" GROUP BY d.id_tipo_despesa ");
        Query query = entity.createNativeQuery(stringQuery.toString());
        query.setParameter(1, idIgreja);
        try {
            List<Object[]> result = (List<Object[]>) query.getResultList();
            LancamentosIndicadoresDTO lancamentosInd = null;
            for (Object[] l : result) {
                lancamentosInd = new LancamentosIndicadoresDTO();
                lancamentosInd.setTipo((String) l[0]);
                lancamentosInd.setValorTotal((Double) l[1]);
                retorno.add(lancamentosInd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }

    @GetMapping("/indicadoresContribuicoes={idIgreja}")
    @PreAuthorize("hasAnyRole('USUARIO_MASTER','USUARIO_ADMINISTRADOR')")
    public List<LancamentosIndicadoresDTO> obterIndicadoresContribuicoesPorIgreja(@PathVariable Integer idIgreja) {
        List<LancamentosIndicadoresDTO> retorno = new ArrayList<LancamentosIndicadoresDTO>();
        StringBuilder stringQuery = new StringBuilder()
                .append("SELECT (SELECT t.nome FROM public.tipo_contribuicao t ")
                .append("   WHERE t.id=d.id_tipo_contribuicao) tipo_contribuicao,sum(d.valor) FROM public.contribuicao d ")
                .append(" WHERE d.id_igreja=?1 ")
                .append("   AND EXTRACT(MONTH FROM d.data_contribuicao) = EXTRACT(MONTH FROM current_timestamp) ")
                .append(" GROUP BY d.id_tipo_contribuicao ");
        Query query = entity.createNativeQuery(stringQuery.toString());
        query.setParameter(1, idIgreja);
        try {
            List<Object[]> result = (List<Object[]>) query.getResultList();
            LancamentosIndicadoresDTO lancamentosInd = null;
            for (Object[] l : result) {
                lancamentosInd = new LancamentosIndicadoresDTO();
                lancamentosInd.setTipo((String) l[0]);
                lancamentosInd.setValorTotal((Double) l[1]);
                retorno.add(lancamentosInd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }

}
