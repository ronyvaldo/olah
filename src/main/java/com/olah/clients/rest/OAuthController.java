package com.olah.clients.rest;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.olah.clients.config.security.JwtProvider;
import com.olah.clients.model.dominio.DominioTipoRedeSocial;
import com.olah.clients.model.dto.TokenDTO;
import com.olah.clients.model.entity.Usuario;
import com.olah.clients.model.entity.UsuarioRedeSocial;
import com.olah.clients.model.repository.UsuarioRedeSocialRepository;
import com.olah.clients.model.repository.UsuarioRepository;
import com.olah.clients.service.UsuarioRedeSocialService;
import com.olah.clients.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;

@RestController
@RequestMapping("/api/oauth")
public class OAuthController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private UsuarioService service;

    @Autowired
    private UsuarioRedeSocialService serviceRedeSocial;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Value("${secretPsw}")
    String secretPsw;

    @Value("${google.clientId}")
    String googleClientId;
    Usuario usuario = new Usuario();

    @PostMapping("/google")
    public ResponseEntity<?> google(@RequestBody TokenDTO tokenDTO) throws IOException {
        final NetHttpTransport transport = new NetHttpTransport();
        final JacksonFactory jacksonFactory = new JacksonFactory();
        GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
                .setAudience(Collections.singletonList(googleClientId));
        final GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), tokenDTO.getValue());
        final GoogleIdToken.Payload payload = googleIdToken.getPayload();
        return tratarUsuarioGoogle(payload);
    }

    @PostMapping("/facebook")
    public ResponseEntity<?> facebook(@RequestBody TokenDTO tokenDTO) throws IOException {
        Facebook facebook = new FacebookTemplate(tokenDTO.getValue());
        //final String[] fields = {"email","picture"};
        User user = facebook.fetchObject("me", User.class/*, fields*/);
        return tratarUsuarioFacebook(user);
    }

    private TokenDTO login(Usuario usuario) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuario.getEmail(), secretPsw));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setValue(jwt);
        return tokenDTO;
    }

    private Usuario salvarUsuario(String email, String nome) {
        Usuario usuarioContext = new Usuario(0,nome,email,this.secretPsw);
        return this.service.salvar(usuarioContext);
    }

    private void cadastrarRelacionamentoRedeSocial(Usuario usuario, Integer redeSocial) {
        UsuarioRedeSocial usuarioRedeSocial = new UsuarioRedeSocial();
        usuarioRedeSocial.setUsuario(usuario);
        usuarioRedeSocial.setTipo(redeSocial);
        usuarioRedeSocial.setEmail(usuario.getEmail());
        try {
            this.serviceRedeSocial.salvar(usuarioRedeSocial);
        } catch (Exception e) {}
    }

    private ResponseEntity<?> tratarUsuarioGoogle(final GoogleIdToken.Payload payload) {
        TokenDTO tokenRes = null;
        if (repository.existsByEmail(payload.getEmail())) {
            this.usuario = repository.findByEmail(payload.getEmail()).get();
        } else {
            this.usuario = salvarUsuario(payload.getEmail(), (String) payload.get("name"));
            cadastrarRelacionamentoRedeSocial(this.usuario, DominioTipoRedeSocial.GOOGLE.getCodigo());
        }
        tokenRes = login(this.usuario);
        return new ResponseEntity(tokenRes, HttpStatus.OK);
    }

    private ResponseEntity<?> tratarUsuarioFacebook(final User user) {
        TokenDTO tokenRes = null;
        System.out.println(user);
        if (repository.existsByEmail(user.getEmail())) {
            this.usuario = repository.findByEmail(user.getEmail()).get();
        } else {
            this.usuario = salvarUsuario(user.getEmail(), user.getName());
            cadastrarRelacionamentoRedeSocial(this.usuario, DominioTipoRedeSocial.Facebook.getCodigo());
        }
        tokenRes = login(this.usuario);
        return new ResponseEntity(tokenRes, HttpStatus.OK);
    }
}
