package com.olah.clients.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name = "igreja")
@Entity
@Data
public class Igreja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "data_cadastro", updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone="GMT-3")
    private Date dataCadastro;

    @OneToOne
    @JoinColumn(name = "id_usuario_cadastro")
    private Usuario usuarioCadastro;

    @OneToOne
    @JoinColumn(name = "id_grupo_congregacional")
    private GrupoCongregacional grupoCongregacional;

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
