package com.olah.clients.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name = "tipo_despesa")
@Entity
@Data
public class TipoDespesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "data_cadastro")
    private Date dataCadastro = new Date();

    @OneToOne
    @JoinColumn(name = "id_usuario_cadastro")
    private Usuario usuarioCadastro;

    @OneToOne
    @JoinColumn(name = "id_grupo_congregacional")
    private GrupoCongregacional grupoCongregacional;

    @OneToOne
    @JoinColumn(name = "id_igreja")
    private Igreja igreja;
}
