package com.olah.clients.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(name = "contribuicao")
@Entity
@Data
public class Contribuicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "id_tipo_contribuicao")
    @NotNull(message = "Você deve selecionar um tipo de contribuição")
    private TipoContribuicao tipoContribuicao;

    @OneToOne
    @JoinColumn(name = "id_usuario")
    @NotNull(message = "Você deve informar um membro")
    private Usuario membro;

    @Column(nullable = false)
    private Double valor;

    @Column(name = "data_cadastro", updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone="GMT-3")
    private Date dataCadastro;

    @Column(name = "data_contribuicao")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone="GMT-3")
    @NotNull(message = "Você deve informar a data da contribuição")
    private Date dataContribuicao;

    @OneToOne
    @JoinColumn(name = "id_usuario_cadastro")
    private Usuario usuarioCadastro;

    @OneToOne
    @JoinColumn(name = "id_igreja")
    private Igreja igreja;

    @Column(name = "data_inativacao")
    private Date dataInativacao;

    @OneToOne
    @JoinColumn(name = "id_usuario_inativacao")
    private Usuario usuarioInativacao;

    @PrePersist
    private void prePersist() {
        setDataCadastro(new Date());
    }
}
