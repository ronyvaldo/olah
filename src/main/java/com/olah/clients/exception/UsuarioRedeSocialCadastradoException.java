package com.olah.clients.exception;

public class UsuarioRedeSocialCadastradoException extends RuntimeException {

    public UsuarioRedeSocialCadastradoException(String credencial) {
        super("Usuário de rede social com o login " +  credencial + " já existe na base de dados do sistema.");
    }
}
