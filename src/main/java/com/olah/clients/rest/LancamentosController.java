package com.olah.clients.rest;

import com.olah.clients.model.dto.LancamentosDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentosController {

    @Autowired
    private EntityManager entity;

    @GetMapping("/porIgreja={idIgreja}")
    @PreAuthorize("hasAnyRole('USUARIO_MASTER','USUARIO_ADMINISTRADOR')")
    public LancamentosDTO obterLancamentosPorIgreja(@PathVariable Integer idIgreja) {
        StringBuilder stringQuery = new StringBuilder()
                .append("SELECT totalContribuicoes, totalDespesas from (SELECT sum(c.valor) totalContribuicoes FROM public.contribuicao c ")
                .append("   WHERE c.id_igreja=?1 and c.data_contribuicao >= CURRENT_DATE - 30) a, ")
                .append("(SELECT sum(d.valor) totalDespesas FROM public.despesa d ")
                .append("   WHERE d.id_igreja=?2 and d.data_despesa >= CURRENT_DATE - 30) b");
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
        return lancamentos;
    }
}
