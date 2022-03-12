package com.olah.clients.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //.antMatchers("/api/usuarios/**").hasRole("USER")
                //hasAnyRole quando mais de uma Role
                .antMatchers("/api/usuarios/autocadastro/**","/api/oauth/**","/api/igrejas/likeNome=**", "/swagger-ui.html",
                        "/api/senha/**")
                .permitAll()
                .antMatchers("/api/usuarios/**","/api/igrejas/**","/api/contribuicoes/**","/api/despesas/**",
                        "/api/tiposContribuicao/**", "/api/lancamentos/**", "/api/tiposDespesa/**", "/api/profissoes/**",
                        "/api/eventos/**", "/api/grupos/**", "/api/eventoUsuario/**", "/api/relatorios/**")
                .authenticated()
                .anyRequest().denyAll();
    }
}
