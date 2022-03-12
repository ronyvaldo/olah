package com.olah.clients.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name = "evento")
@Entity
@Data
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nome;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dataInicio;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dataTermino;

    @JsonFormat(pattern = "HH:mm")
    private Date horarioInicio;

    @JsonFormat(pattern = "HH:mm")
    private Date horarioTermino;

    @Column(name = "data_cadastro")
    private Date dataCadastro = new Date();

    @OneToOne
    @JoinColumn(name = "id_usuario_cadastro")
    private Usuario usuarioCadastro;

    private Integer idadeMinima;

    private Integer idadeMaxima;

    @OneToOne
    @JoinColumn(name = "id_igreja")
    private Igreja igreja;

    @Column(name = "data_inativacao")
    private Date dataInativacao;

    @OneToOne
    @JoinColumn(name = "id_usuario_inativacao")
    private Usuario usuarioInativacao;

}
