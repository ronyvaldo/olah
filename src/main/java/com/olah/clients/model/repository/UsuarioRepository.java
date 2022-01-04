package com.olah.clients.model.repository;

import com.olah.clients.model.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Usuario findByLogin(String login);
    Optional<Usuario> findByEmail(String email);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
    Page<Usuario> findByPerfil(Integer perfil, Pageable pageable);
    List<Usuario> findByPerfilAndNomeLike(Integer perfil, String nome);

}
