package com.olah.clients.model.entity;
import com.olah.clients.model.dominio.DominioTipoRedeSocial;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Table(name = "usuario_rede_social")
@Entity
@Data
public class UsuarioRedeSocial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @Email
    private String email;

    private Integer tipo;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Transient
    private String descricaoRedeSocial = getDescricaoRedeSocial();

    public String getDescricaoRedeSocial() {
        if (getTipo() != null) {
            return DominioTipoRedeSocial.getDescricaoRedeSocial(getTipo());
        }
        return null;
    }
}
