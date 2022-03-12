package com.olah.clients.exception;

public class TipoDespesaException extends RuntimeException {

    public TipoDespesaException(String nome) {
        super("Tipo de Despesa com nome " +  nome + " já existe na base de dados do sistema.");
    }

}
