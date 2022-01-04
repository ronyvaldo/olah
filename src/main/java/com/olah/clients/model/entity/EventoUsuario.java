package com.olah.clients.model.entity;

import lombok.Data;

import javax.persistence.*;

@Table(name = "evento_usuario")
@Entity
@Data
public class EventoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @OneToOne
    @JoinColumn(name = "id_evento")
    private Evento evento;
}
