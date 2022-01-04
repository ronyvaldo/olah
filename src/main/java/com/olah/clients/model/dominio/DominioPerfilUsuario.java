package com.olah.clients.model.dominio;

public enum DominioPerfilUsuario {

    USUARIO("Usuário Comum"),
    MEMBRO("Membro"),
    ADMINISTRADOR("Administrador"),
    MASTER("Master");

    private String descricao;

    DominioPerfilUsuario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static String getDescricaoPerfil(Integer perfil) {
        for (DominioPerfilUsuario value : DominioPerfilUsuario.values()) {
            if (value.ordinal() == perfil) {
                return value.getDescricao();
            }
        }
        return null;
    }

}
