package com.olah.clients.model.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class LancamentosDTO {

    private Double totalContribuicoes;
    private Double totalDespesas;
    private BigInteger totalDeMembros;
    private BigInteger totalEntregaramContribuicoes;

}
