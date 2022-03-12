package com.olah.clients.model.dto;

import lombok.Data;

@Data
public class AtualizacaoSenhaDTO {

    private Integer idUsuario;
    private String senhaAtual;
    private String novaSenha;
}
