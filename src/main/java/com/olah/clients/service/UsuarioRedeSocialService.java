package com.olah.clients.service;

import com.olah.clients.exception.UsuarioCadastradoException;
import com.olah.clients.exception.UsuarioRedeSocialCadastradoException;
import com.olah.clients.model.entity.UsuarioRedeSocial;
import com.olah.clients.model.repository.UsuarioRedeSocialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioRedeSocialService {

    @Autowired
    private UsuarioRedeSocialRepository repository;

    public UsuarioRedeSocial salvar(UsuarioRedeSocial usuarioRedeSocial) {
        List<UsuarioRedeSocial> lista = repository.findByEmail(usuarioRedeSocial.getUsuario().getEmail());
        Boolean isExists = false;
        for (UsuarioRedeSocial i : lista) {
            if (i.getTipo() == usuarioRedeSocial.getTipo())
                isExists = true;
                break;
        }
        if (isExists) {
            throw new UsuarioRedeSocialCadastradoException(usuarioRedeSocial.getUsuario().getLogin());
        }
        return repository.save(usuarioRedeSocial);
    }
}
