package com.olah.clients.model.entity;

import lombok.Data;

import javax.persistence.*;

@Table(name = "profissao")
@Entity
@Data
public class Profissao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String area;

    @Column(nullable = false)
    private String nome;
}
