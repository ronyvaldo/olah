package com.olah.clients.service;

import com.olah.clients.model.dominio.DominioRole;
import com.olah.clients.model.entity.Usuario;
import com.olah.clients.exception.UsuarioCadastradoException;
import com.olah.clients.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    public Usuario salvar(Usuario usuario) {
        boolean isExists = repository.existsByEmail(usuario.getLogin());
        if (isExists) {
            throw new UsuarioCadastradoException(usuario.getEmail());
        }
        isExists = repository.existsByLogin(usuario.getLogin());
        if (isExists) {
            throw new UsuarioCadastradoException(usuario.getLogin());
        }
        return repository.save(usuario);
    }

    @Override
    public UserDetails loadUserByUsername(String credencial) throws UsernameNotFoundException {
        Usuario usuario = repository.findByLogin(credencial);
        if (usuario == null || usuario.getId() == null) {
            usuario = repository.findByEmail(credencial)
                    .orElseThrow(() -> new UsernameNotFoundException("Login não encontrado."));
        }
        /*Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Login não encontrado."));*/
        return User.builder().username(usuario.getLogin())
                .password(usuario.getSenha()).roles(DominioRole.getRole(usuario.getPerfil()))
                .build();
    }
}
