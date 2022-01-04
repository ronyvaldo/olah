package com.olah.clients.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.olah.clients.model.dominio.DominioPerfilUsuario;
import com.olah.clients.validation.groups.NotNullField;
import com.olah.clients.validation.sequence.CpfUsuarioNotNullFieldProvider;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;
import org.hibernate.validator.group.GroupSequenceProvider;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "usuario")
@Entity
@Data
@GroupSequenceProvider(value = CpfUsuarioNotNullFieldProvider.class)
public class Usuario {

    public Usuario(Integer perfil, String nome, String email, String senha) {
        this.perfil = perfil;
        this.nome = nome;
        this.email = email;
        this.nome = nome;
        this.senha = senha;
    }

    public Usuario() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String login;

    @Column(nullable = false, length = 1)
    private Integer perfil;

    @Transient
    @JsonIgnore
    private String descricaoPerfil = getDescricaoPerfil();

    @Column(nullable = false, unique = true)
    @NotEmpty(message = "{campo.email.obrigatorio}")
    @Email(message = "{campo.email.invalido}")
    private String email;

    @Column(nullable = false, length = 150)
    @NotEmpty(message = "{campo.nome.obrigatorio}")
    private String nome;

    @Column(nullable = true, length = 11)
    @CPF(message = "{campo.cpf.invalido}", groups = NotNullField.class)
    private String cpf;

    private String senha;

    @Column(name = "ddd_celular")
    private String dddCelular;

    @Column(name = "numero_celular")
    private String numeroCelular;

    @Column(name = "data_conversao")
    private Date dataConversao;

    @OneToOne
    @JoinColumn(name = "id_grupo_congregacional")
    private GrupoCongregacional grupoCongregacional;

    @Column(name = "permite_cadastrar_admin")
    private Boolean permiteCadastrarAdministrador;

    @Column(name = "permite_cadastrar_igreja")
    private Boolean permiteCadastrarIgreja;

    @Column(name = "data_cadastro", updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone="GMT-3")
    private Date dataCadastro;

    @Column(name = "data_inativacao")
    private Date dataInativacao;

    @Column(name = "data_nascimento")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone="GMT-3")
    private Date dataNascimento;

    @OneToOne
    @JoinColumn(name = "id_usuario_inativacao")
    private Usuario usuarioInativacao;

    @OneToOne
    @JoinColumn(name = "id_usuario_cadastro")
    private Usuario usuarioCadastro;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name="igreja_usuario",
            joinColumns={@JoinColumn(name = "id_usuario")},
            inverseJoinColumns={@JoinColumn(name = "id_igreja")})
    private List<Igreja> igrejas;

    @Transient
    @JsonIgnore
    private List<UsuarioRedeSocial> redesSociais;

    @PrePersist
    private void prePersist() {
        setDataCadastro(new Date());
    }

    public String getLogin() {
        if (this.login == null) {
            return getEmail();
        }
        return this.login;
    }

    public String getDescricaoPerfil() {
        if (getPerfil() != null) {
            return DominioPerfilUsuario.getDescricaoPerfil(getPerfil());
        }
        return null;
    }

    public List<UsuarioRedeSocial> getRedesSociais() {
        if (this.redesSociais == null) {
            this.redesSociais = new ArrayList<>();
        }
        return this.redesSociais;
    }

    @JsonIgnore
    public String getSenha() {
        return this.senha;
    }

    @JsonProperty
    public void setSenha(String senha) {
        this.senha = senha;
    }

}
