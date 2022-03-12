package com.olah.clients.exception;

public class GrupoCongregacionalCadastradoException extends RuntimeException {

    public GrupoCongregacionalCadastradoException(String nome) {
        super("Grupo Congregacional com nome " +  nome + " já existe na base de dados do sistema.");
    }
}
