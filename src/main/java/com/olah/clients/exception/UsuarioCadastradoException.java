package com.olah.clients.exception;

public class UsuarioCadastradoException extends RuntimeException {

    public UsuarioCadastradoException(String credencial) {
        super("Usuário com o login " +  credencial + " já existe na base de dados do sistema.");
    }
}
