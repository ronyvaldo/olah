package com.olah.clients.exception;

public class EventoUsuarioCadastradoException extends RuntimeException {

    public EventoUsuarioCadastradoException(String nome) {
        super("Você já está inscrito no evento " +  nome + ".");
    }
}
