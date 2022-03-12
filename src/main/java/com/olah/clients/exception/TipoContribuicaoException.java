package com.olah.clients.exception;

public class TipoContribuicaoException extends RuntimeException {

    public TipoContribuicaoException(String nome) {
        super("Tipo de Contribuicao com nome " +  nome + " já existe na base de dados do sistema.");
    }
}
