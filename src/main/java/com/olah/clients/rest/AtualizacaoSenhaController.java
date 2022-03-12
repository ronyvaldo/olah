package com.olah.clients.rest;

import com.olah.clients.model.dto.AtualizacaoSenhaDTO;
import com.olah.clients.model.entity.Usuario;
import com.olah.clients.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/senha")
public class AtualizacaoSenhaController {

    @Autowired
    private EntityManager entity;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PutMapping("/atualizar")
    @Transactional
    public void atualizar(@RequestBody AtualizacaoSenhaDTO atualizacaoSenhaDTO) {
        Usuario usuarioAtualizacao = null;
        Optional<Usuario> retorno = usuarioRepository.findById(atualizacaoSenhaDTO.getIdUsuario());
        if (retorno.isPresent()) {
            usuarioAtualizacao = retorno.get();
        }
        if (usuarioAtualizacao != null && atualizacaoSenhaDTO.getSenhaAtual() != null && atualizacaoSenhaDTO.getSenhaAtual().equals(usuarioAtualizacao.getSenha())) {
            entity.createQuery("update Usuario set senha='"+atualizacaoSenhaDTO.getNovaSenha()+"' where id ="+atualizacaoSenhaDTO.getIdUsuario()).executeUpdate();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha atual informada é inválida.");
        }
    }
}
