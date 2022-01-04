package com.olah.clients.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name = "despesa")
@Entity
@Data
public class Despesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "id_tipo_despesa")
    private TipoDespesa tipoDespesa;

    @Column(nullable = false)
    private Double valor;

    @Column(name = "data_cadastro", updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone="GMT-3")
    private Date dataCadastro;

    @Column(name = "data_despesa")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone="GMT-3")
    private Date dataDespesa;

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

    private String observacao;

    @PrePersist
    private void prePersist() {
        setDataCadastro(new Date());
    }
}
