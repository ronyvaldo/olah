package com.olah.clients.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

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

    @Column(name = "data_inscricao", updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone="GMT-3")
    private Date dataInscricao;

    @PrePersist
    private void prePersist() {
        setDataInscricao(new Date());
    }
}
