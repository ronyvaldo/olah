package com.olah.clients.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name = "endereco_usuario")
@Entity
@Data
public class EnderecoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(nullable = false)
    private String cep;

    @Column(nullable = false)
    private String uf;

    @Column(nullable = false)
    private String logradouro;

    private String numero;

    @Column(nullable = false)
    private String bairro;

    @Column(nullable = false)
    private String cidade;

    private String complemento;

    @Column(name = "data_cadastro")
    private Date dataCadastro = new Date();

}
