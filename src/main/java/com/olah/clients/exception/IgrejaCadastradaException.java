package com.olah.clients.exception;

public class IgrejaCadastradaException extends RuntimeException {

    public IgrejaCadastradaException(String nome) {
        super("Igreja com nome " +  nome + " já existe na base de dados do sistema.");
    }
}
